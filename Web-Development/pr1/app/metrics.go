package app

import (
	"context"
	"encoding/json"
	"log"
	"net/http"
	"runtime"
	"sync/atomic"
	"time"

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
	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()
	go WorkerMetrics(ctx, ws)
	for {
		_, _, err := ws.ReadMessage()
		if err != nil {
			log.Println("web socket client disconnected")
			break
		}
	}
}

var RequestsPerSec atomic.Int64
var prevRequestsPerSec atomic.Int64

type Metric struct {
	Ram        int `json:"ram"`
	Goroutines int `json:"goroutines"`
	Requests   int `json:"requests"`
}

func NewMetric() Metric {
	m := Metric{}
	m.Goroutines = runtime.NumGoroutine()
	var memStats runtime.MemStats
	runtime.ReadMemStats(&memStats)
	m.Ram = int(memStats.HeapAlloc)
	currReqs := RequestsPerSec.Load()
	prevreqs := prevRequestsPerSec.Swap(currReqs)
	m.Requests = int(currReqs - prevreqs)
	return m
}

func WorkerMetrics(ctx context.Context, ws *websocket.Conn) {
	ticker := time.NewTicker(time.Second)
	for {
		select {
		case <-ticker.C:
			m := NewMetric()
			data, err := json.Marshal(m)
			if err != nil {
				log.Println(err)
				continue
			}
			err = ws.WriteMessage(websocket.TextMessage, data)
			if err != nil {
				log.Println(err)
				return
			}
		case <-ctx.Done():
			return
		}
	}
}
