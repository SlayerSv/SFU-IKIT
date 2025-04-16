package main

import (
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net/http"
)

// downloadJSON fetches JSON data from the given URL
func downloadJSON(url string) ([]byte, error) {
	resp, err := http.Get(url)
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		err := fmt.Errorf("unexpected status code: %d", resp.StatusCode)
		return nil, err
	}

	data, err := io.ReadAll(resp.Body)
	if err != nil {
		return nil, err
	}
	return data, nil
}

// parseJSON unmarshals JSON into ExchangeRateData and converts to records
func parseJSON(data []byte) (map[string]Currency, error) {
	var response Response
	err := json.Unmarshal(data, &response)
	if err != nil {
		return nil, err
	}
	return response.Data, nil
}

func main() {
	cfg, err := NewConfig()
	if err != nil {
		log.Fatalf("ERROR: create config: %v", err)
	}
	logger, err := NewFileLogger(cfg.LogFilePath)
	if err != nil {
		log.Fatalf("ERROR: create logger: %v", err)
	}
	db, err := NewPostgres(cfg.DBConn)
	if err != nil {
		log.Fatalf("ERROR: create database: %v", err)
	}
	app := NewApp(cfg, db, logger)
	app.Log.Println("INFO: start Currency App")

	// Download JSON
	data, err := downloadJSON(app.Config.APIURL)
	if err != nil {
		logger.Fatalf("ERROR: download JSON: %v", err)
	}
	app.Log.Println("INFO: download json currencies")
	// Parse JSON
	currencies, err := parseJSON(data)
	if err != nil {
		logger.Fatalf("ERROR: parse JSON: %v", err)
	}
	app.Log.Println("INFO: parse json currencies")
	// Insert currencies
	err = app.DB.InsertCurrencies(currencies)
	if err != nil {
		logger.Fatalf("ERROR: insert currencies to database: %v", err)
	}
	app.Log.Println("INFO: insert currencies to database")
	logger.Println("INFO: exit Currency App")
}
