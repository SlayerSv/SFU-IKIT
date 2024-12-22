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
var liked []map[int]bool

func genLike(t time.Time) error {
	if liked == nil {
		liked = make([]map[int]bool, len(users))
	}
	if len(posts) == 0 {
		return errNoPosts
	}
	user, post := 0, 0
	for {
		user = rand.IntN(len(users))
		post = rand.IntN(len(posts))
		if liked[user] == nil {
			liked[user] = make(map[int]bool)
		}
		_, alreadyLiked := liked[user][post]
		if alreadyLiked {
			continue
		}
		liked[user][post] = true
		break
	}
	like := Like{
		userID:     user,
		postID:     post,
		created_at: t,
	}
	likes = append(likes, like)
	return nil
}
