package main

import (
	"time"
)

type Thread struct {
	userID     int
	created_at time.Time
}

var threads = []Thread{}
