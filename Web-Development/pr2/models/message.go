package models

type Message struct {
	Name    string   `json:"name"`
	Message string   `json:"message"`
	Users   []string `json:"users,omitempty"`
	Date    string   `json:"date"`
}
