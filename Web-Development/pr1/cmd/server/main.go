package main

import (
	"net/http"

	"web/app"
)

func main() {
	router := app.NewRouter()
	http.ListenAndServe("localhost:8080", router)
}
