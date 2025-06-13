package broker

import (
	"time"
)

type Message struct {
	Value string
	Time  time.Time
}

type Broker interface {
	GetTopic() string
}

type Writer interface {
	Broker
	Send(message Message) error
}

type Reader interface {
	Broker
	Read() (Message, error)
}
