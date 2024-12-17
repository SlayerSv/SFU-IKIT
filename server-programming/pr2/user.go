package main

import (
	"math/rand/v2"
	"strings"
	"time"
)

type User struct {
	name       string
	created_at time.Time
}

var users = []User{}
var male_names = []string{"Alexandr", "Sergey", "Ivan", "Pavel", "Nikita", "Artem", "Nikolay"}
var female_names = []string{"Maria", "Anna", "Alena", "Anastasia", "Oksana", "Svetlana", "Daria"}
var surnames = []string{"Ivanov", "Petrov", "Sidorov", "Komarov", "Medvedev", "Volkov", "Malcev"}

func genUsers(t time.Time) time.Time {
	var builder strings.Builder
	for i := range male_names {
		user := User{}
		builder.WriteString(male_names[i])
		builder.WriteByte(' ')
		builder.WriteString(surnames[i])
		user.name = builder.String()
		t.Add(time.Second + time.Duration(rand.IntN(9)+1))
		user.created_at = t
		users = append(users, user)
	}
	for i := range female_names {
		user := User{}
		builder.WriteString(female_names[i])
		builder.WriteByte(' ')
		builder.WriteString(surnames[i])
		builder.WriteByte('a')
		user.name = builder.String()
		t.Add(time.Second + time.Duration(rand.IntN(9)+1))
		user.created_at = t
		users = append(users, user)
	}
	return t
}
