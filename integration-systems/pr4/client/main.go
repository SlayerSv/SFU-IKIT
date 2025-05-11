package main

import (
	"context"
	"log"
	"time"

	"github.com/SlayerSv/SFU-IKIT/integration/pr4/broker"
	"github.com/SlayerSv/SFU-IKIT/integration/pr4/broker/kafka"
	pb "github.com/SlayerSv/SFU-IKIT/integration/pr4/server/proto"

	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
)

var NewCurrencies []broker.Message

func main() {
	cfg, err := NewConfig()
	if err != nil {
		log.Fatalf("ERROR: Read config: %v", err)
	}
	kr := kafka.NewReader(cfg.KafkaAddr, cfg.KafkaTopic)
	log.Println("INFO: Starting client")
	ListenAndRead(cfg.ServerAddr, kr)
}

func ListenAndRead(serverAddr string, br broker.Reader) {
	conn, err := grpc.NewClient(serverAddr, grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		log.Fatalf("failed to connect: %v", err)
	}
	defer conn.Close()
	client := pb.NewCurrencyServiceClient(conn)
	log.Println("INFO: listening for kafka messages")
	for {
		msg, err := br.Read()
		if err != nil {
			log.Printf("ERROR: Read kafka message: %v", err)
			continue
		}
		log.Printf("INFO: Received broker message %s topic %s", msg, br.GetTopic())
		NewCurrencies = append(NewCurrencies, msg)
		go GetCurrency(client, msg.Value)
	}
}

func GetCurrency(client pb.CurrencyServiceClient, code string) {
	ctx, cancel := context.WithTimeout(context.Background(), time.Second*2)
	defer cancel()
	req := &pb.GetCurrencyRequest{Code: code}
	resp, err := client.GetCurrency(ctx, req)
	if err != nil {
		log.Printf("ERROR: Failed to get currency %s: %v", code, err)
		return
	}
	log.Printf("INFO: Received currency from server through gRPC: %v", resp)
}
