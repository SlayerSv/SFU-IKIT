package main

import (
	"errors"
	"math/rand/v2"
	"time"
)

type Post struct {
	userID     int
	threadID   int
	body       string
	created_at time.Time
}

var posts = []Post{}
var errNoThreads = errors.New("no threads")

const postTextLength = 40

func genPost(t time.Time) error {
	if len(threads) == 0 {
		return errNoThreads
	}
	user := rand.IntN(len(users))
	thread := rand.IntN(len(threads))
	post := Post{
		userID:     user,
		threadID:   thread,
		body:       genRandomString(postTextLength),
		created_at: t,
	}
	posts = append(posts, post)
	return nil
}
