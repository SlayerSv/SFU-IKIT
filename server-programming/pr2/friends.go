package main

import (
	"math/rand/v2"
	"time"
)

type Friends struct {
	user1ID    int
	user2ID    int
	created_at time.Time
}

var friends = []Friends{}

func genFriends(t time.Time) time.Time {
	var taken = make([][]int, len(users))
	for i := range taken {
		taken[i] = make([]int, len(users))
	}
	i := len(users) / 2
	for i > 0 {
		friend1 := rand.IntN(len(users))
		friend2 := rand.IntN(len(users))
		if friend1 == friend2 {
			continue
		}
		if taken[friend1][friend2] != 0 {
			continue
		}
		t = t.Add(time.Second * time.Duration(rand.IntN(100)+1))
		frnds := Friends{
			friend1,
			friend2,
			t,
		}
		friends = append(friends, frnds)
		taken[friend1][friend2] = 1
		taken[friend2][friend1] = 1
		i--
	}
	return t
}
