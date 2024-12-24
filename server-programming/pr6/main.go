package main

import (
	"log"
	"net/http"
)

func main() {
	http.HandleFunc("GET /users/", get)
	http.HandleFunc("GET /users/{id}", getByID)
	http.HandleFunc("POST /users/", add)
	http.HandleFunc("PUT /users/", edit)
	http.HandleFunc("DELETE /users/{id}", delete)
	log.Println("Starting server...")
	log.Fatalln(http.ListenAndServe("localhost:8080", nil))
}
