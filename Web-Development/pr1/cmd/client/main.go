package main

import (
	"bytes"
	"encoding/json"
	"flag"
	"fmt"
	"log"
	"net/http"
	"time"

	"web/models"
)

func main() {
	workers := flag.Int("w", 1, "Number of workers to make requests to server")
	times := flag.Int("t", 5, "Duration of server loading with requests")
	flag.Parse()
	for range *workers {
		go worker()
	}
	time.Sleep(time.Second * time.Duration(*times))
}

var client = http.Client{}

func worker() {
	for {
		user := models.User{Name: "user", Password: "password"}
		body, err := json.Marshal(user)
		if err != nil {
			log.Println(err)
			continue
		}
		postReq, err := http.NewRequest(http.MethodPost, "http://localhost:8080/users", bytes.NewBuffer(body))
		postReq.Header.Set("Content-Type", "application/json")
		if err != nil {
			log.Println(err)
			continue
		}
		postResp, err := client.Do(postReq)
		if err != nil {
			log.Println(err)
			continue
		}
		if postResp.StatusCode != 201 {
			log.Printf("Error: expected 201 status code. Got: %d. Resp: %v", postResp.StatusCode, postResp)
			continue
		}
		var createdUser models.User
		err = json.NewDecoder(postResp.Body).Decode(&createdUser)
		if err != nil {
			log.Println(err)
			continue
		}
		postResp.Body.Close()

		var getUser models.User
		getReq, err := http.NewRequest(http.MethodGet, fmt.Sprintf("http://localhost:8080/users/%d", createdUser.ID), nil)
		if err != nil {
			log.Println(err)
			continue
		}
		getResp, err := client.Do(getReq)
		if err != nil {
			log.Println(err)
			continue
		}
		err = json.NewDecoder(getResp.Body).Decode(&getUser)
		if err != nil {
			log.Println(err)
			continue
		}
		getResp.Body.Close()
		editedUser := getUser
		editedUser.Name = "user1"
		body, err = json.Marshal(editedUser)
		if err != nil {
			log.Println(err)
			continue
		}

		patchReq, err := http.NewRequest(http.MethodPatch, "http://localhost:8080/users", bytes.NewBuffer(body))
		if err != nil {
			log.Println(err)
			continue
		}
		patchResp, err := client.Do(patchReq)
		if err != nil {
			log.Println(err)
			continue
		}
		var patchUser models.User
		err = json.NewDecoder(patchResp.Body).Decode(&patchUser)
		if err != nil {
			log.Println(err)
			continue
		}
		patchResp.Body.Close()

		delReq, err := http.NewRequest(http.MethodDelete, fmt.Sprintf("http://localhost:8080/users/%d", createdUser.ID), nil)
		if err != nil {
			log.Println(err)
			continue
		}
		delResp, err := client.Do(delReq)
		if err != nil {
			log.Println(err)
			continue
		}
		var delUser models.User
		err = json.NewDecoder(delResp.Body).Decode(&delUser)
		if err != nil {
			log.Println(err)
			continue
		}
		delResp.Body.Close()
	}
}
