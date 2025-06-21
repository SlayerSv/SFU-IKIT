package app

import (
	"encoding/json"
	"errors"
	"log"
	"net/http"
	"net/url"
	"slices"
	"sync"
	"time"

	"pr2/models"

	"github.com/gorilla/websocket"
)

var clients map[string]*websocket.Conn = make(map[string]*websocket.Conn)
var mu sync.RWMutex

var upgrader = websocket.Upgrader{
	ReadBufferSize:  1024,
	WriteBufferSize: 1024,
	CheckOrigin:     func(r *http.Request) bool { return true },
}

func WSConnections(w http.ResponseWriter, r *http.Request) {
	cookie, err := r.Cookie("chat-user-name")
	if err != nil {
		errorJSON(w, r, errors.New("unauthorized"))
		return
	}
	name, err := url.QueryUnescape(cookie.Value)
	if err != nil {
		log.Println(err)
		return
	}
	_, ok := clients[name]
	if ok {
		log.Printf("user %s already logged in", name)
		http.Redirect(w, r, "/login", http.StatusSeeOther)
		return
	}
	ws, err := upgrader.Upgrade(w, r, nil)
	if err != nil {
		log.Println(err)
		return
	}
	defer ws.Close()

	addClient(name, ws)
	for {
		_, m, err := ws.ReadMessage()
		if err != nil {
			log.Println("web socket client disconnected")
			RemoveClient(name, ws)
			break
		}
		message := models.Message{
			Name:    name,
			Message: string(m),
			Date:    time.Now().Format(time.DateTime),
		}
		sendMessage(message)
	}
}

func addClient(name string, ws *websocket.Conn) {
	message := models.Message{
		Name:    name,
		Message: "Joined the chat",
		Date:    time.Now().Format(time.DateTime),
	}
	var users []string
	mu.Lock()
	clients[name] = ws
	for name := range clients {
		users = append(users, name)
	}
	slices.Sort(users)
	message.Users = users
	mu.Unlock()
	sendMessage(message)
}

func RemoveClient(name string, ws *websocket.Conn) {
	message := models.Message{
		Name:    name,
		Message: "Left the chat",
		Date:    time.Now().Format(time.DateTime),
	}
	var users []string
	mu.Lock()
	delete(clients, name)
	for name := range clients {
		users = append(users, name)
	}
	message.Users = users
	mu.Unlock()
	sendMessage(message)
}

func sendMessage(message models.Message) {
	data, err := json.Marshal(message)
	if err != nil {
		log.Printf("Error marshalling message for sending: %v", err)
		return
	}
	mu.RLock()
	defer mu.RUnlock()
	for _, ws := range clients {
		err = ws.WriteMessage(websocket.TextMessage, data)
		if err != nil {
			log.Printf("Error sending message: %v", err)
		}
	}
}
