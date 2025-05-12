package main

import (
	"errors"
	"log"
	"net"
	"net/http"
	"os"

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
	logger := log.New(os.Stdout, "", log.Flags())
	db, err := NewPostgres()
	if err != nil {
		log.Fatalf("ERROR: Create database: %v", err)
	}
	kafka := kafka.NewWriter(os.Getenv("KAFKA_ADDR"), os.Getenv("KAFKA_TOPIC"))

	app := NewApp(db, logger, kafka)

	// starting gRPC server in separate goroutine
	lis, err := net.Listen("tcp", os.Getenv("GRPC_PORT"))
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
	if _, err := app.DB.GetCurrencyByCode("USD"); err != nil && errors.Is(err, errNotFound) {
		currencies, err := app.GetCurrenciesFromAPI()
		if err != nil {
			logger.Fatalf("ERROR: Download JSON: %v", err)
		}
		err = app.DB.InsertCurrencies(currencies)
		if err != nil {
			logger.Fatalf("ERROR: Insert currencies to database: %v", err)
		}
		app.Log.Println("INFO: Inserted currencies to database")
	}

	// starting REST server
	err = http.ListenAndServe(os.Getenv("PORT"), app.NewRouter())
	if err != nil {
		logger.Fatalf("ERROR: Starting server: %v", err)
	}
	logger.Println("INFO: Exited Currency App")
}
