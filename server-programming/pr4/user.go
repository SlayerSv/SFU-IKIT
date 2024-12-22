package main

import (
	"fmt"
	"time"
)

type User struct {
	ID         int
	Name       string
	Created_at time.Time
}

func (u User) String() string {
	return fmt.Sprintf("ID: %d\nName: %s\nCreated at: %s\n",
		u.ID, u.Name, u.Created_at.Format("02.01.2006 15:04:05"))
}
