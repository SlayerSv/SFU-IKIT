package main

import (
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net/http"

	"integration/pr4/broker"
	"integration/pr4/broker/kafka"
)

var NewCurrencies []broker.Message

func main() {
	cfg, err := NewConfig()
	if err != nil {
		log.Fatalf("ERROR: Read config: %v", err)
	}
	cfg.ServerAPIKey = GetApiKey(cfg.ServerAddr)
	kr := kafka.NewReader(cfg.KafkaAddr, cfg.KafkaTopic)
	ListenAndRead(cfg.ServerAddr, cfg.ServerAPIKey, kr)
}

func ListenAndRead(serverAddr, server_api_key string, br broker.Reader) {
	for {
		msg, err := br.Read()
		if err != nil {
			log.Printf("ERROR: Read kafka message: %v", err)
			continue
		}
		log.Printf("INFO: Received broker message %s topic %s", msg, br.GetTopic())
		NewCurrencies = append(NewCurrencies, msg)
		GetCurrency(serverAddr, server_api_key, msg.Value)
	}
}

func GetCurrency(serverAddr, serserver_api_key, code string) {
	url := fmt.Sprintf("http://%s/currencies/%s?api_key=%s", serverAddr, code, serserver_api_key)
	resp, err := http.Get(url)
	if err != nil {
		log.Printf("ERROR: Get currency from server: %v", err)
		return
	}
	msg, err := io.ReadAll(resp.Body)
	if err != nil {
		log.Printf("ERROR: Read response body from server: %v", err)
		return
	}
	if resp.StatusCode != 200 {
		log.Printf("ERROR: Get currency from server code: %d body: %s", resp.StatusCode, string(msg))
		return
	}
	log.Printf("INFO: Received currency from server:\n%s", string(msg))
}

func GetApiKey(serverAddr string) string {
	url := fmt.Sprintf("http://%s/api_key", serverAddr)
	resp, err := http.Get(url)
	if err != nil {
		log.Printf("ERROR: Get api key from server: %v", err)
		return ""
	}
	decoder := json.NewDecoder(resp.Body)
	var APIKey struct {
		APIKey string `json:"api_key"`
	}
	err = decoder.Decode(&APIKey)
	if err != nil {
		log.Printf("ERROR: decode api key: %v", err)
		return ""
	}
	if resp.StatusCode != 201 {
		log.Printf("ERROR: Get api key from server code: %d", resp.StatusCode)
		return ""
	}
	return APIKey.APIKey
}
