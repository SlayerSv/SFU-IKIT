package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
)

type App struct {
	Config *Config
	DB     DB
	Log    *log.Logger
}

type DB interface {
	SetupDatabase() error
	InsertCurrencies(currencies map[string]Currency) error
}

func NewApp(cfg *Config, db DB, log *log.Logger) *App {
	return &App{
		Config: cfg,
		DB:     db,
		Log:    log,
	}
}

func (app *App) GetCurrencies() (map[string]Currency, error) {
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
