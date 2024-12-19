package main

import (
	"math/rand/v2"
	"time"
)

type Thread struct {
	userID     int
	created_at time.Time
}

var threads = []Thread{}

func genThread(t time.Time) {
	user := rand.IntN(len(users))
	thread := Thread{
		userID:     user,
		created_at: t,
	}
	threads = append(threads, thread)
}
