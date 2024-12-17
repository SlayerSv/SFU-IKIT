package main

import (
	"time"
)

type Post struct {
	userID     int
	threadID   int
	created_at time.Time
}

var posts = []Post{}
