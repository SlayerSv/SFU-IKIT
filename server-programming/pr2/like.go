package main

import (
	"time"
)

type Like struct {
	userID     int
	postID     int
	created_at time.Time
}

var likes = []Like{}
