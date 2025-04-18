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
	AddCurrency(Currency) error
	GetCurrencyCount() (int, error)
	HasAPIKey(key string) (bool, error)
	AddAPIKey(key string) error
	UpdatedAt() time.Time
}

func NewApp(cfg *Config, db DB, log *log.Logger) *App {
	return &App{
		Config: cfg,
		DB:     db,
		Log:    log,
	}
}

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

func (app *App) GetCurrencies(w http.ResponseWriter, r *http.Request) {
	c, err := app.DB.GetCurrencies()
	if err != nil {
		app.Log.Printf("ERROR: Get currencies from db: %v", err)
		w.WriteHeader(http.StatusInternalServerError)
		return
	}
	err = json.NewEncoder(w).Encode(c)
	if err != nil {
		app.Log.Printf("ERROR: Encode currencies json: %v", err)
		w.WriteHeader(http.StatusInternalServerError)
		return
	}
	app.Log.Println("INFO: Sent all currencies")
}

func (app *App) GetCurrencyByCode(w http.ResponseWriter, r *http.Request) {
	code := r.PathValue("code")
	if code == "" || len(code) != 3 {
		app.Log.Printf("INFO: Invalid currency code in request %s", r.URL)
		w.WriteHeader(http.StatusBadRequest)
		return
	}
	c, err := app.DB.GetCurrencyByCode(code)
	if errors.Is(err, errNotFound) {
		app.Log.Printf("INFO: Currency %s not found", code)
		w.WriteHeader(http.StatusNotFound)
		return
	}
	if err != nil {
		app.Log.Printf("ERROR: Get currency %s from db: %v", code, err)
		w.WriteHeader(http.StatusInternalServerError)
		return
	}
	err = json.NewEncoder(w).Encode(c)
	if err != nil {
		app.Log.Printf("ERROR: encode currency %s json: %v", code, err)
		w.WriteHeader(http.StatusInternalServerError)
		return
	}
	app.Log.Printf("INFO: Sent currency %s", c.Code)
}

func (app *App) GetCurrencyCount(w http.ResponseWriter, r *http.Request) {
	c, err := app.DB.GetCurrencyCount()
	if err != nil {
		app.Log.Printf("ERROR: Get currency count from db: %v", err)
		w.WriteHeader(http.StatusInternalServerError)
		return
	}
	err = json.NewEncoder(w).Encode(struct {
		Count int `json:"currencies_count"`
	}{Count: c})
	if err != nil {
		app.Log.Printf("ERROR: Encode currency count json: %v", err)
		w.WriteHeader(http.StatusInternalServerError)
		return
	}
	app.Log.Printf("INFO: Sent currency count %d", c)
}

func (app *App) AddCurrency(w http.ResponseWriter, r *http.Request) {
	var c Currency
	err := json.NewDecoder(r.Body).Decode(&c)
	if err != nil {
		app.Log.Printf("ERROR: Decode currency from request: %v", err)
		w.WriteHeader(http.StatusBadRequest)
		return
	}
	err = app.DB.AddCurrency(c)
	if err != nil {
		if errors.Is(err, errAlreadyExists) {
			app.Log.Printf("INFO: Add currency %s to db: already exists", c.Code)
			w.WriteHeader(http.StatusConflict)
			return
		}
		app.Log.Printf("ERROR: Add currency to db: %v", err)
		w.WriteHeader(http.StatusInternalServerError)
		return
	}
	w.WriteHeader(http.StatusCreated)
	app.Log.Printf("INFO: Added new currency %s", c.Code)
}

func (app *App) UpdatedAt(w http.ResponseWriter, r *http.Request) {
	t := app.DB.UpdatedAt()
	err := json.NewEncoder(w).Encode(struct {
		Updated_at time.Time `json:"updated_at"`
	}{Updated_at: t})
	if err != nil {
		app.Log.Printf("ERROR: Encode updated at json: %v", err)
		w.WriteHeader(http.StatusInternalServerError)
		return
	}
	app.Log.Printf("INFO: Sent updated_at info")
}

func (app *App) GetAPIKey(w http.ResponseWriter, r *http.Request) {
	app.Log.Printf("INFO: Incoming request %s", r.URL)
	b := make([]byte, 32)
	_, err := rand.Read(b)
	if err != nil {
		app.Log.Printf("ERROR: Generate hash: %v", err)
		w.WriteHeader(http.StatusInternalServerError)
		return
	}
	apikey := base64.URLEncoding.EncodeToString(b)
	w.WriteHeader(http.StatusCreated)
	w.Header().Set("Content-Type", "application/json")
	err = json.NewEncoder(w).Encode(struct {
		APIKey string `json:"api_key"`
	}{APIKey: apikey})
	if err != nil {
		app.Log.Printf("ERROR: Encode API key: %v", err)
		w.WriteHeader(http.StatusInternalServerError)
		return
	}
	err = app.DB.AddAPIKey(apikey)
	if err != nil {
		app.Log.Printf("ERROR: Add API key to db: %v", err)
		w.WriteHeader(http.StatusInternalServerError)
		return
	}
	app.Log.Printf("INFO: Created and served new API key %s", apikey)
}

func (app *App) Auth(next http.HandlerFunc) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		app.Log.Printf("INFO: Incoming request %s", r.URL)
		w.Header().Set("Content-Type", "application/json")
		key := r.URL.Query().Get("api_key")
		if key == "" {
			app.Log.Printf("INFO: Missing api key in request %s", r.URL)
			w.WriteHeader(http.StatusUnauthorized)
			return
		}
		has, err := app.DB.HasAPIKey(key)
		if err != nil {
			app.Log.Printf("ERROR: Check api key in db: %v", err)
			w.WriteHeader(http.StatusInternalServerError)
			return
		}
		if !has {
			app.Log.Printf("INFO: Invalid api key in request %s", r.URL)
			w.WriteHeader(http.StatusUnauthorized)
			return
		}
		next.ServeHTTP(w, r)
	}
}
