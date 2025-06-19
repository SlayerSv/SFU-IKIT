package app

import (
	"context"
	"log"
	"net/http"

	"github.com/gorilla/websocket"
)

var upgrader = websocket.Upgrader{
	ReadBufferSize:  1024,
	WriteBufferSize: 1024,
	CheckOrigin:     func(r *http.Request) bool { return true },
}

func WSConnections(w http.ResponseWriter, r *http.Request) {
	ws, err := upgrader.Upgrade(w, r, nil)
	if err != nil {
		log.Println(err)
		return
	}
	defer ws.Close()
	_, cancel := context.WithCancel(context.Background())
	defer cancel()
	for {
		_, _, err := ws.ReadMessage()
		if err != nil {
			log.Println("web socket client disconnected")
			break
		}
	}
}
