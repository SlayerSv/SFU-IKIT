package main

import (
	"errors"
	"math/rand/v2"
	"time"
)

type Like struct {
	postID     int
	userID     int
	created_at time.Time
}

var likes = []Like{}
var errNoPosts = errors.New("no posts")

func genLike(t time.Time) error {
	if len(posts) == 0 {
		return errNoPosts
	}
	user := rand.IntN(len(users))
	post := rand.IntN(len(posts))
	like := Like{
		userID:     user,
		postID:     post,
		created_at: t,
	}
	likes = append(likes, like)
	return nil
}
