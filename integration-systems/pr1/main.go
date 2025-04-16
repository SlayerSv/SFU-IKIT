package main

import (
	"log"
)

func main() {
	cfg, err := NewConfig()
	if err != nil {
		log.Fatalf("ERROR: Create config: %v", err)
	}
	logger, err := NewFileLogger(cfg.LogFilePath)
	if err != nil {
		log.Fatalf("ERROR: Create logger: %v", err)
	}
	db, err := NewPostgres(cfg.DBConn)
	if err != nil {
		log.Fatalf("ERROR: Create database: %v", err)
	}
	app := NewApp(cfg, db, logger)
	app.Log.Println("INFO: Started Currency App")

	currencies, err := app.GetCurrencies()
	if err != nil {
		logger.Fatalf("ERROR: Download JSON: %v", err)
	}
	err = app.DB.InsertCurrencies(currencies)
	if err != nil {
		logger.Fatalf("ERROR: Insert currencies to database: %v", err)
	}
	app.Log.Println("INFO: Inserted currencies to database")
	logger.Println("INFO: Exited Currency App")
}
