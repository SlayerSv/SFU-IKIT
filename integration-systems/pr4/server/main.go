package main

import (
	"log"
	"net"
	"net/http"

	"github.com/SlayerSv/SFU-IKIT/integration/pr4/broker/kafka"
)

// @title Currency API
// @version 1.0
// @description A RESTful API for managing currency data, supporting API key authentication and CRUD operations.
// @openapi 3.0.0
// @host localhost:8080
// @BasePath /
// @securityDefinitions.apikey ApiKeyAuth
// @in header
// @name Authorization
func main() {
	// setup
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

	// starting gRPC server in separate goroutine
	lis, err := net.Listen("tcp", "localhost:50051")
	if err != nil {
		app.Log.Fatalf("ERROR: Listening to tcp: %v", err)
	}
	g, err := NewGRPCServer(db)
	if err != nil {
		app.Log.Fatalf("ERROR: Creating gRPC server: %v", err)
	}
	go g.Serve(lis)

	// getting currencies from outside api
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

	// starting REST server
	err = http.ListenAndServe("localhost:8080", app.NewRouter())
	if err != nil {
		logger.Fatalf("ERROR: Starting server: %v", err)
	}
	logger.Println("INFO: Exited Currency App")
}
