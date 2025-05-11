package main

import (
	"encoding/json"
	"os"
)

type Config struct {
	ServerAddr string `json:"server_addr"`
	KafkaAddr  string `json:"kafka_addr"`
	KafkaTopic string `json:"kafka_topic"`
}

func NewConfig() (Config, error) {
	cfgFile, err := os.Open("client/config.json")
	var cfg Config
	if err != nil {
		return cfg, err
	}
	err = json.NewDecoder(cfgFile).Decode(&cfg)
	if err != nil {
		return cfg, err
	}
	return cfg, nil
}
