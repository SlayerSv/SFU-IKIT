package app

import (
	"net/http"
)

func NewRouter() *http.ServeMux {
	mux := http.NewServeMux()
	mux.HandleFunc("GET /login", LoginForm)
	mux.HandleFunc("POST /login", Login)
	mux.HandleFunc("GET /signup", SignupForm)
	mux.HandleFunc("POST /signup", Signup)
	mux.HandleFunc("POST /logout", Logout)
	mux.HandleFunc("GET /chat", Chat)
	mux.HandleFunc("GET /users/{user_id}", GetByID)
	mux.HandleFunc("POST /users", Add)
	mux.HandleFunc("PATCH /users", Edit)
	mux.HandleFunc("DELETE /users/{user_id}", Delete)
	mux.HandleFunc("/ws", WSConnections)
	return mux
}
