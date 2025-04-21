package client

import (
	"encoding/json"
	"os"
)

type Config struct {
	BrokerAddress string
	Topic         string
}

func NewConfig() (Config, error) {
	cfgFile, err := os.Open("config.json")
	var cfg Config
	if err != nil {
		return cfg, err
	}
	err = json.NewEncoder(cfgFile).Encode(&cfg)
	if err != nil {
		return cfg, err
	}
	return cfg, nil
}
