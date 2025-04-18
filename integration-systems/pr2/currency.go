package main

import (
	"time"
)

type Response struct {
	Data map[string]Currency `json:"data"`
}

// Currency represents a currency entity
// @Description Currency data with code, name, and symbols
type Currency struct {
	Code         string `json:"code"`
	Name         string `json:"name"`
	NamePlural   string `json:"name_plural"`
	Symbol       string `json:"symbol"`
	SymbolNative string `json:"symbol_native"`
}

// ErrorResponse represents an error message
// @Description Error response with a message
type ErrorResponse struct {
	Message string `json:"message"`
}

// CountResponse represents the count of currencies
// @Description Response with the number of currencies
type CountResponse struct {
	Count int `json:"currencies_count"`
}

// UpdatedAtResponse represents the last update timestamp
// @Description Response with the last update timestamp
type UpdatedAtResponse struct {
	UpdatedAt time.Time `json:"updated_at"`
}

// APIKeyResponse represents a generated API key
// @Description Response with a new API key
type APIKeyResponse struct {
	APIKey string `json:"api_key"`
}
