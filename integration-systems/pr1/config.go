package main

import (
	"encoding/json"
	"fmt"
	"os"
)

type Config struct {
	APIURL      string `json:"api_url"`
	DBConn      string `json:"db_conn"`
	LogFilePath string `json:"log_file_path"`
}

func NewConfig() (*Config, error) {
	file, err := os.Open("config.json")
	if err != nil {
		return nil, fmt.Errorf("open config.json file: %w", err)
	}
	var cfg = &Config{}
	decoder := json.NewDecoder(file)
	decoder.DisallowUnknownFields()
	err = decoder.Decode(cfg)
	if err != nil {
		return nil, fmt.Errorf("decode config.json file: %w", err)
	}
	return cfg, nil
}
