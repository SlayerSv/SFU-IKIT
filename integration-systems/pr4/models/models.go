package models

import (
	"fmt"
	"time"
)

type Response struct {
	Data map[string]Currency `json:"data"`
}

// CurrencyT represents a currency model with timestamps
// @Description Currency data with timestamps
type Currency struct {
	CurrencyD
	CreatedAt time.Time `json:"created_at"`
	UpdatedAt time.Time `json:"updated_at"`
}

// Currency represents a currency model
// @Description Currency data
type CurrencyD struct {
	Code         string `json:"code"`
	Name         string `json:"name"`
	NamePlural   string `json:"name_plural"`
	Symbol       string `json:"symbol"`
	SymbolNative string `json:"symbol_native"`
}

func (c CurrencyD) String() string {
	return fmt.Sprintf("Code: %s\nName: %s\nNamePlural: %s\nSymbol: %s\nSymbol native:%s\n", c.Code, c.Name, c.NamePlural, c.Symbol, c.SymbolNative)
}

func (c Currency) String() string {
	return fmt.Sprintf("%sCreated at: %s\nUpdated at: %s\n", c.CurrencyD.String(), c.CreatedAt, c.UpdatedAt)
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
