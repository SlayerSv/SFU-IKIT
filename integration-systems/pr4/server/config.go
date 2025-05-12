package main

import (
	"encoding/json"
	"fmt"
	"os"
)

type Config struct {
	APIURL      string `json:"api_url"`
	APIKey      string `json:"api_key"`
	DBConn      string `json:"db_conn"`
	ServerAddr  string `json:"server_addr"`
	LogFilePath string `json:"log_file_path"`
	KafkaAddr   string `json:"kafka_addr"`
	KafkaTopic  string `json:"kafka_topic"`
}

func NewConfig() (*Config, error) {
	file, err := os.Open("server/config.json")
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
