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
var male_names = []string{"Alexandr", "Sergey", "Ivan", "Pavel", "Nikita", "Artem", "Nikolay",
	"Albert", "Petr", "Victor", "Gennadiy", "Anton", "Vadim", "Vladimir", "Dmitriy", "Boris",
	"Dmitriy", "Igor", "Semen", "Leonid"}
var female_names = []string{"Maria", "Anna", "Alena", "Anastasia", "Oksana", "Svetlana", "Daria",
	"Natalia", "Marina", "Nina", "Elena", "Kira", "Karina", "Zlata", "Zhanna", "Zoya", "Lubov",
	"Victoria", "Taisia", "Yana"}
var surnames = []string{"Ivanov", "Petrov", "Sidorov", "Komarov", "Medvedev", "Volkov", "Malcev",
	"Lebedev", "Popov", "Smirnov", "Kuznecov", "Sokolov", "Novikov", "Morozov", "Pavlov", "Semenov",
	"Zubarev", "Isaev", "Noskov", "Kabanov"}

func genUsers(t time.Time) time.Time {
	var builder strings.Builder
	for i := range male_names {
		user := User{}
		builder.WriteString(male_names[i])
		builder.WriteByte(' ')
		builder.WriteString(surnames[i])
		user.name = builder.String()
		t = t.Add(time.Second * time.Duration(rand.IntN(9)+1))
		user.created_at = t
		users = append(users, user)
		builder.Reset()
	}
	for i := range female_names {
		user := User{}
		builder.WriteString(female_names[i])
		builder.WriteByte(' ')
		builder.WriteString(surnames[i])
		builder.WriteByte('a')
		user.name = builder.String()
		t = t.Add(time.Second + time.Duration(rand.IntN(9)+1))
		user.created_at = t
		users = append(users, user)
		builder.Reset()
	}
	return t
}
