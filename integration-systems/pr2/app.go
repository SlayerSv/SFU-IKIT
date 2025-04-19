package main

import (
	"crypto/rand"
	"encoding/base64"
	"encoding/json"
	"errors"
	"fmt"
	"log"
	"net/http"
	"time"
)

var errNotFound = errors.New("not found")
var errAlreadyExists = errors.New("already exists")
var errInternal = errors.New("internal server error")

type App struct {
	Config *Config
	DB     DB
	Log    *log.Logger
}

type DB interface {
	SetupDatabase() error
	InsertCurrencies(currencies map[string]Currency) error
	GetCurrencies() ([]Currency, error)
	GetCurrencyByCode(code string) (Currency, error)
	InsertCurrency(Currency) (Currency, error)
	GetCurrencyCount() (int, error)
	HasAPIKey(key string) (bool, error)
	AddAPIKey(key string) error
	UpdatedAt() (time.Time, error)
}

func NewApp(cfg *Config, db DB, log *log.Logger) *App {
	return &App{
		Config: cfg,
		DB:     db,
		Log:    log,
	}
}

// GetCurrenciesFromAPI fetches currencies from an external API
func (app *App) GetCurrenciesFromAPI() (map[string]Currency, error) {
	url := fmt.Sprintf("%s?apikey=%s", app.Config.APIURL, app.Config.APIKey)
	resp, err := http.Get(url)
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return nil, fmt.Errorf("unexpected status code: %d", resp.StatusCode)
	}
	app.Log.Printf("INFO: Downloaded json currencies from %s", app.Config.APIURL)
	var response Response
	err = json.NewDecoder(resp.Body).Decode(&response)
	if err != nil {
		return nil, err
	}
	app.Log.Println("INFO: Parsed currencies")
	return response.Data, nil
}

// @Summary Get all currencies
// @Description Retrieves a list of all currency entities
// @Tags Currencies
// @Produce json
// @Security ApiKeyAuth
// @Success 200 {array} Currency
// @Failure 401 {object} ErrorResponse
// @Failure 500 {object} ErrorResponse
// @Router /currencies [get]
func (app *App) GetCurrencies(w http.ResponseWriter, r *http.Request) {
	c, err := app.DB.GetCurrencies()
	e := json.NewEncoder(w)
	if err != nil {
		app.Log.Printf("ERROR: Get currencies from db: %v", err)
		w.WriteHeader(http.StatusInternalServerError)
		e.Encode(ErrorResponse{Message: errInternal.Error()})
		return
	}
	err = e.Encode(c)
	if err != nil {
		app.Log.Printf("ERROR: Encode currencies json: %v", err)
		w.WriteHeader(http.StatusInternalServerError)
		e.Encode(ErrorResponse{Message: errInternal.Error()})
		return
	}
	app.Log.Println("INFO: Sent all currencies")
}

// @Summary Get a currency by code
// @Description Retrieves a currency entity by its unique code
// @Tags Currencies
// @Produce json
// @Security ApiKeyAuth
// @Param code path string true "The currency code (e.g., USD, EUR)"
// @Success 200 {object} Currency
// @Failure 400 {object} ErrorResponse
// @Failure 401 {object} ErrorResponse
// @Failure 404 {object} ErrorResponse
// @Failure 500 {object} ErrorResponse
// @Router /currencies/{code} [get]
func (app *App) GetCurrencyByCode(w http.ResponseWriter, r *http.Request) {
	code := r.PathValue("code")
	e := json.NewEncoder(w)
	if code == "" || len(code) != 3 {
		app.Log.Printf("INFO: Invalid currency code in request %s", r.URL)
		w.WriteHeader(http.StatusBadRequest)
		e.Encode(ErrorResponse{Message: "invalid currency code"})
		return
	}
	c, err := app.DB.GetCurrencyByCode(code)
	if errors.Is(err, errNotFound) {
		app.Log.Printf("INFO: Currency %s not found", code)
		w.WriteHeader(http.StatusNotFound)
		e.Encode(ErrorResponse{Message: "not found"})
		return
	}
	if err != nil {
		app.Log.Printf("ERROR: Get currency %s from db: %v", code, err)
		w.WriteHeader(http.StatusInternalServerError)
		e.Encode(ErrorResponse{Message: errInternal.Error()})
		return
	}
	err = e.Encode(c)
	if err != nil {
		app.Log.Printf("ERROR: encode currency %s json: %v", code, err)
		w.WriteHeader(http.StatusInternalServerError)
		e.Encode(ErrorResponse{Message: errInternal.Error()})
		return
	}
	app.Log.Printf("INFO: Sent currency %s", c.Code)
}

// @Summary Get the number of currencies
// @Description Returns the total count of currency entities
// @Tags Currencies
// @Produce json
// @Security ApiKeyAuth
// @Success 200 {object} CountResponse
// @Failure 401 {object} ErrorResponse
// @Failure 500 {object} ErrorResponse
// @Router /currencies/count [get]
func (app *App) GetCurrencyCount(w http.ResponseWriter, r *http.Request) {
	c, err := app.DB.GetCurrencyCount()
	e := json.NewEncoder(w)
	if err != nil {
		app.Log.Printf("ERROR: Get currency count from db: %v", err)
		w.WriteHeader(http.StatusInternalServerError)
		e.Encode(ErrorResponse{Message: errInternal.Error()})
		return
	}
	err = e.Encode(CountResponse{Count: c})
	if err != nil {
		app.Log.Printf("ERROR: Encode currency count json: %v", err)
		w.WriteHeader(http.StatusInternalServerError)
		e.Encode(ErrorResponse{Message: errInternal.Error()})
		return
	}
	app.Log.Printf("INFO: Sent currency count %d", c)
}

// @Summary Add a new currency
// @Description Adds a new currency entity to the dataset
// @Tags Currencies
// @Accept json
// @Produce json
// @Security ApiKeyAuth
// @Param currency body CurrencyD true "Currency object to add"
// @Success 201 {object} Currency
// @Failure 400 {object} ErrorResponse
// @Failure 401 {object} ErrorResponse
// @Failure 409 {object} ErrorResponse
// @Failure 500 {object} ErrorResponse
// @Router /currencies [post]
func (app *App) AddCurrency(w http.ResponseWriter, r *http.Request) {
	var c Currency
	err := json.NewDecoder(r.Body).Decode(&c)
	e := json.NewEncoder(w)
	if err != nil {
		app.Log.Printf("ERROR: Decode currency from request: %v", err)
		w.WriteHeader(http.StatusBadRequest)
		e.Encode(ErrorResponse{Message: "invalid currency format"})
		return
	}
	newCur, err := app.DB.InsertCurrency(c)
	if err != nil {
		if errors.Is(err, errAlreadyExists) {
			app.Log.Printf("INFO: Add currency %s to db: already exists", c.Code)
			w.WriteHeader(http.StatusConflict)
			e.Encode(ErrorResponse{Message: errAlreadyExists.Error()})
			return
		}
		app.Log.Printf("ERROR: Add currency to db: %v", err)
		w.WriteHeader(http.StatusInternalServerError)
		e.Encode(ErrorResponse{Message: errInternal.Error()})
		return
	}
	w.WriteHeader(http.StatusCreated)
	e.Encode(newCur)
	app.Log.Printf("INFO: Added new currency %s", newCur.Code)
}

// @Summary Get the last update timestamp
// @Description Returns the date and time when the currency dataset was last updated
// @Tags Currencies
// @Produce json
// @Security ApiKeyAuth
// @Success 200 {object} UpdatedAtResponse
// @Failure 401 {object} ErrorResponse
// @Failure 500 {object} ErrorResponse
// @Router /currencies/updated_at [get]
func (app *App) UpdatedAt(w http.ResponseWriter, r *http.Request) {
	t, err := app.DB.UpdatedAt()
	e := json.NewEncoder(w)
	if err != nil {
		app.Log.Printf("ERROR: Get updated at: %v", err)
		w.WriteHeader(http.StatusInternalServerError)
		e.Encode(ErrorResponse{Message: errInternal.Error()})
		return
	}
	err = e.Encode(UpdatedAtResponse{UpdatedAt: t})
	if err != nil {
		app.Log.Printf("ERROR: Encode updated at json: %v", err)
		w.WriteHeader(http.StatusInternalServerError)
		e.Encode(ErrorResponse{Message: errInternal.Error()})
		return
	}
	app.Log.Printf("INFO: Sent updated_at info")
}

// @Summary Generate a new API key
// @Description Creates and returns a new API key for authentication
// @Tags Authentication
// @Produce json
// @Success 201 {object} APIKeyResponse
// @Failure 500 {object} ErrorResponse
// @Router /api_key [get]
func (app *App) GetAPIKey(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	app.Log.Printf("INFO: Incoming request %s", r.URL)
	b := make([]byte, 32)
	_, err := rand.Read(b)
	e := json.NewEncoder(w)
	if err != nil {
		app.Log.Printf("ERROR: Generate hash: %v", err)
		w.WriteHeader(http.StatusInternalServerError)
		e.Encode(ErrorResponse{Message: errInternal.Error()})
		return
	}
	apikey := base64.URLEncoding.EncodeToString(b)
	w.WriteHeader(http.StatusCreated)
	err = e.Encode(APIKeyResponse{APIKey: apikey})
	if err != nil {
		app.Log.Printf("ERROR: Encode API key: %v", err)
		w.WriteHeader(http.StatusInternalServerError)
		e.Encode(ErrorResponse{Message: errInternal.Error()})
		return
	}
	err = app.DB.AddAPIKey(apikey)
	if err != nil {
		app.Log.Printf("ERROR: Add API key to db: %v", err)
		w.WriteHeader(http.StatusInternalServerError)
		e.Encode(ErrorResponse{Message: errInternal.Error()})
		return
	}
	app.Log.Printf("INFO: Created and served new API key %s", apikey)
}

// @Summary Authenticate requests
// @Description Middleware to validate API key in query parameter
// @Tags Authentication
// @Produce json
// @Security ApiKeyAuth
func (app *App) Auth(next http.HandlerFunc) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "application/json")
		app.Log.Printf("INFO: Incoming request %s", r.URL)
		key := r.URL.Query().Get("api_key")
		e := json.NewEncoder(w)
		if key == "" {
			app.Log.Printf("INFO: Missing api key in request %s", r.URL)
			w.WriteHeader(http.StatusUnauthorized)
			e.Encode(ErrorResponse{Message: "missing API key"})
			return
		}
		has, err := app.DB.HasAPIKey(key)
		if err != nil {
			app.Log.Printf("ERROR: Check api key in db: %v", err)
			w.WriteHeader(http.StatusInternalServerError)
			e.Encode(ErrorResponse{Message: errInternal.Error()})
			return
		}
		if !has {
			app.Log.Printf("INFO: Invalid api key in request %s", r.URL)
			w.WriteHeader(http.StatusUnauthorized)
			e.Encode(ErrorResponse{Message: "invalid API key"})
			return
		}
		next.ServeHTTP(w, r)
	}
}

func (app *App) RecoverPanic(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		defer func() {
			if err := recover(); err != nil {
				app.Log.Printf("ERROR: panic: %v", err)
				w.WriteHeader(http.StatusInternalServerError)
				json.NewEncoder(w).Encode(ErrorResponse{Message: errInternal.Error()})
			}
		}()
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
		w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization")
		next.ServeHTTP(w, r)
	})
}
