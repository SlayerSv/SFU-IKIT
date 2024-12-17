package main

import (
	"os"
)

func writeFiles() {
	usersFile, err := os.OpenFile("users.csv", os.O_CREATE|os.O_RDWR|os.O_TRUNC, 0666)
	if err != nil {
		panic("Error opening users file")
	}
	friendsFile, err := os.OpenFile("friends.csv", os.O_CREATE|os.O_RDWR|os.O_TRUNC, 0666)
	if err != nil {
		panic("Error opening friends file")
	}
	threadsFile, err := os.OpenFile("threads.csv", os.O_CREATE|os.O_RDWR|os.O_TRUNC, 0666)
	if err != nil {
		panic("Error opening threads file")
	}
	postsFile, err := os.OpenFile("posts.csv", os.O_CREATE|os.O_RDWR|os.O_TRUNC, 0666)
	if err != nil {
		panic("Error opening posts file")
	}
	likesFile, err := os.OpenFile("likes.csv", os.O_CREATE|os.O_RDWR|os.O_TRUNC, 0666)
	if err != nil {
		panic("Error opening likes file")
	}
}
