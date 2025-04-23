package main

import (
	"fmt"
	"io"
	"log"
	"net/http"

	"integration/pr4/broker"
	"integration/pr4/broker/kafka"
)

func main() {
	cfg, err := NewConfig()
	if err != nil {
		log.Fatalf("ERROR: Read config: %v", err)
	}
	kr := kafka.NewReader(cfg.KafkaAddr, cfg.KafkaTopic)
	ListenAndRead(cfg.KafkaAddr, kr)
}

func ListenAndRead(serverAddr string, br broker.Reader) {
	for {
		msg, err := br.Read()
		if err != nil {
			log.Printf("ERROR: Read kafka message: %v", err)
			continue
		}
		log.Printf("INFO: Received broker message %s topic %s", msg, br.GetTopic())
		GetCurrency(serverAddr, msg.Value)
	}
}

func GetCurrency(serverAddr, code string) {
	url := fmt.Sprintf("%s/currencies/%s", serverAddr, code)
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
	log.Printf("INFO: Received currency from server:\n%s", string(msg))
}
