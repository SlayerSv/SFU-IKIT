package main

type Response struct {
	Data map[string]Currency `json:"data"`
}

// Currency represents the JSON structure
type Currency struct {
	Code         string `json:"code"`
	Name         string `json:"name"`
	NamePlural   string `json:"name_plural"`
	Symbol       string `json:"symbol"`
	SymbolNative string `json:"symbol_native"`
}
