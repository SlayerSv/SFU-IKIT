package main

import (
	"log"
	"net/http"

	"pr6/app"
)

func main() {
	http.HandleFunc("GET /users/", app.Get)
	http.HandleFunc("GET /users/{id}", app.GetByID)
	http.HandleFunc("POST /users/", app.Add)
	http.HandleFunc("PUT /users/", app.Edit)
	http.HandleFunc("DELETE /users/{id}", app.Delete)
	log.Println("Starting server...")
	log.Fatalln(http.ListenAndServe("localhost:8080", nil))
}
