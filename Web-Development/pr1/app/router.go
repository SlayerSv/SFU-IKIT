package app

import (
	"net/http"
)

func NewRouter() *http.ServeMux {
	mux := http.NewServeMux()
	mux.HandleFunc("GET /metrics", Metrics)
	mux.HandleFunc("GET /users/{user_id}", GetByID)
	mux.HandleFunc("POST /users", Add)
	mux.HandleFunc("PATCH /users", Edit)
	mux.HandleFunc("DELETE /users/{user_id}", Delete)
	mux.HandleFunc("/ws", WSConnections)
	return mux
}
