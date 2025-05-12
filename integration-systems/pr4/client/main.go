package main

import (
	"context"
	"log"
	"runtime"
	"time"

	"github.com/SlayerSv/SFU-IKIT/integration/pr4/broker"
	"github.com/SlayerSv/SFU-IKIT/integration/pr4/broker/kafka"
	"github.com/SlayerSv/SFU-IKIT/integration/pr4/models"
	pb "github.com/SlayerSv/SFU-IKIT/integration/pr4/server/proto"

	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
)

var MessageHistory []broker.Message

func main() {
	cfg, err := NewConfig()
	if err != nil {
		log.Fatalf("ERROR: Read config: %v", err)
	}
	kr := kafka.NewReader(cfg.KafkaAddr, cfg.KafkaTopic)
	workers := runtime.NumCPU()
	messages := make(chan broker.Message)
	currencies := make(chan models.Currency)

	conn, err := grpc.NewClient(cfg.ServerAddr, grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		log.Fatalf("failed to connect: %v", err)
	}
	defer conn.Close()
	client := pb.NewCurrencyServiceClient(conn)
	for i := 0; i < workers; i++ {
		go Worker(messages, currencies, client)
	}

	go func() {
		for currency := range currencies {
			log.Printf("Received currency from server %v", currency)
		}
	}()

	log.Println("INFO: Starting client")
	ListenAndRead(cfg.ServerAddr, kr, messages)
}

func ListenAndRead(serverAddr string, br broker.Reader, messages chan<- broker.Message) {
	log.Println("INFO: listening for kafka messages")
	for {
		msg, err := br.Read()
		if err != nil {
			log.Printf("ERROR: Read kafka message: %v", err)
			continue
		}
		log.Printf("INFO: Received broker message %s topic %s", msg, br.GetTopic())
		MessageHistory = append(MessageHistory, msg)
		messages <- msg
	}
}

func Worker(messages <-chan broker.Message, currencies chan<- models.Currency, client pb.CurrencyServiceClient) {
	for msg := range messages {
		ctx, cancel := context.WithTimeout(context.Background(), time.Second*5)
		req := &pb.GetCurrencyRequest{Code: msg.Value}
		resp, err := client.GetCurrency(ctx, req)
		cancel()
		if err != nil {
			log.Printf("ERROR: Failed to get currency %s: %v", req.Code, err)
			return
		}
		createdAt, err := time.Parse(time.RFC3339, resp.CreatedAt)
		if err != nil {
			log.Printf("ERROR: Parsing time created at: %v", err)
			continue
		}
		updatedAt, err := time.Parse(time.RFC3339, resp.UpdatedAt)
		if err != nil {
			log.Printf("ERROR: Parsing time updated at: %v", err)
			continue
		}
		currency := models.Currency{
			CurrencyD: models.CurrencyD{
				Code:         resp.Code,
				Name:         resp.Name,
				NamePlural:   resp.NamePlural,
				Symbol:       resp.Symbol,
				SymbolNative: resp.SymbolNative,
			},
			CreatedAt: createdAt,
			UpdatedAt: updatedAt,
		}
		currencies <- currency
	}
}
