package main

import (
	"net/http"

	"pr2/app"
)

func main() {
	router := app.NewRouter()
	http.ListenAndServe("localhost:8080", router)
}
