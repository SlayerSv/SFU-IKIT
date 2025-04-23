package main

import (
	"log"
	"net/http"

	"integration/pr4/broker/kafka"
)

// @title Currency API
// @version 1.0
// @description A RESTful API for managing currency data, supporting API key authentication and CRUD operations.
// @openapi 3.0.0
// @host localhost:8080
// @BasePath /
// @securityDefinitions.apikey ApiKeyAuth
// @in query
// @name api_key
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
	kafka := kafka.NewWriter(cfg.KafkaAddr, cfg.KafkaTopic)
	app := NewApp(cfg, db, logger, kafka)
	app.Log.Println("INFO: Started Currency App")

	currencies, err := app.GetCurrenciesFromAPI()
	if err != nil {
		logger.Fatalf("ERROR: Download JSON: %v", err)
	}
	err = app.DB.InsertCurrencies(currencies)
	if err != nil {
		logger.Fatalf("ERROR: Insert currencies to database: %v", err)
	}
	app.Log.Println("INFO: Inserted currencies to database")

	err = http.ListenAndServe("localhost:8080", app.NewRouter())
	if err != nil {
		logger.Fatalf("ERROR: starting server: %v", err)
	}
	logger.Println("INFO: Exited Currency App")
}
