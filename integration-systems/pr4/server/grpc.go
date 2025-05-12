package main

import (
	"context"
	"errors"
	"time"

	pb "github.com/SlayerSv/SFU-IKIT/integration/pr4/server/proto"

	"google.golang.org/grpc"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

type gRPC struct {
	pb.UnimplementedCurrencyServiceServer
	DB *PostgresDB
}

func (g *gRPC) GetCurrency(ctx context.Context, in *pb.GetCurrencyRequest) (*pb.GetCurrencyResponse, error) {
	currencyCode := in.GetCode()
	currency, err := g.DB.GetCurrencyByCode(currencyCode)
	if err != nil {
		if errors.Is(err, errNotFound) {
			return nil, status.Errorf(codes.NotFound, "currency %s not found", currencyCode)
		}
		return nil, status.Error(codes.Internal, errInternal.Error())
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
