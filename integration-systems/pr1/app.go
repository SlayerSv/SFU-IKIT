package main

import (
	"log"
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
