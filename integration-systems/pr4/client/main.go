package client

import (
	"integration/pr4/broker"
	"integration/pr4/broker/kafka"

	"log"
)

func main() {
	cfg, err := NewConfig()
	if err != nil {
		log.Fatalf("ERROR: Read config: %v", err)
	}
	kr := kafka.NewReader(cfg.BrokerAddress, cfg.Topic)
	ListenAndRead(kr)
}

func ListenAndRead(br broker.Reader) {
	for {
		msg, err := br.Read()
		if err != nil {
			log.Printf("ERROR: Read kafka message: %v", err)
			continue
		}
		log.Printf("INFO: Received broker message %s topic %s", msg, br.GetTopic())
	}
}
