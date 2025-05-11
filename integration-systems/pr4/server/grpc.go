package main

import (
	"context"
	"time"

	pb "github.com/SlayerSv/SFU-IKIT/integration/pr4/server/proto"
	"google.golang.org/grpc"
)

type gRPC struct {
	pb.UnimplementedCurrencyServiceServer
	DB *PostgresDB
}

func (g *gRPC) GetCurrency(ctx context.Context, in *pb.GetCurrencyRequest) (*pb.GetCurrencyResponse, error) {
	code := in.GetCode()
	currency, err := g.DB.GetCurrencyByCode(code)
	if err != nil {
		return nil, err
	}
	return &pb.GetCurrencyResponse{
		Code:         currency.Code,
		Name:         currency.Name,
		NamePlural:   currency.NamePlural,
		Symbol:       currency.Symbol,
		SymbolNative: currency.SymbolNative,
		CreatedAt:    currency.CreatedAt.Format(time.RFC3339),
		UpdatedAt:    currency.UpdatedAt.Format(time.RFC3339),
	}, nil
}

func NewGRPCServer(db *PostgresDB) (*grpc.Server, error) {
	g := grpc.NewServer()
	pb.RegisterCurrencyServiceServer(g, &gRPC{DB: db})
	return g, nil
}
