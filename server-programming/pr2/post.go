package main

import (
	"errors"
	"math/rand/v2"
	"time"
)

type Post struct {
	userID     int
	threadID   int
	created_at time.Time
}

var posts = []Post{}
var errNoThreads = errors.New("no threads")

func genPost(t time.Time) error {
	if len(threads) == 0 {
		return errNoThreads
	}
	user := rand.IntN(len(users))
	thread := rand.IntN(len(threads))
	post := Post{
		userID:     user,
		threadID:   thread,
		created_at: t,
	}
	posts = append(posts, post)
	return nil
}