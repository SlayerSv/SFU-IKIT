package main

import (
	"math/rand/v2"
	"os"
	"strconv"
	"time"
)

func main() {
	if len(os.Args) < 2 {
		panic("Error: provide number of generated rows")
	}
	rows, err := strconv.Atoi(os.Args[1])
	if err != nil {
		panic("wrong value for number of generated rows, provide integer")
	}
	currTime := time.Date(2024, time.January, 1, 0, 0, 0, 0, time.UTC)
	currTime = genUsers(currTime)
	currTime = genFriends(currTime)
	for rows > 0 {
		currTime = currTime.Add(time.Second * time.Duration(rand.IntN(9)+1))
		action := rand.IntN(100)
		if action < 5 {
			genThread(currTime)
		} else if action < 50 {
			err := genPost(currTime)
			if err != nil {
				continue
			}
		} else {
			err := genLike(currTime)
			if err != nil {
				continue
			}
		}
		rows--
	}
	writeFiles()
}
