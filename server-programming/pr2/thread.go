package main

import (
	"math/rand/v2"
	"strings"
	"time"
)

type Thread struct {
	userID     int
	title      string
	created_at time.Time
}

var threads = []Thread{}

const threadNameLength = 20

func genThread(t time.Time) {
	user := rand.IntN(len(users))
	thread := Thread{
		userID:     user,
		title:      genRandomString(threadNameLength),
		created_at: t,
	}
	threads = append(threads, thread)
}

const letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

func genRandomString(length int) string {
	var b strings.Builder
	b.Grow(length)
	var i int
	for range length {
		i = rand.IntN(len(letters))
		b.WriteByte(letters[i])
	}
	return b.String()
}
