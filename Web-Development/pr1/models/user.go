package models

import (
	"fmt"
	"time"
)

type User struct {
	ID         int       `json:"id"`
	Name       string    `json:"name"`
	Password   string    `json:"password"`
	Created_at time.Time `json:"created_at"`
}

func (u User) String() string {
	return fmt.Sprintf("ID: %d\nName: %s\nCreated at: %s\n",
		u.ID, u.Name, u.Created_at.Format("02.01.2006 15:04:05"))
}
