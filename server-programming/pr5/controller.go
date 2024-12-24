package main

import (
	"flag"
	"fmt"
	"os"
	"strconv"
)

func get() {
	os.Args = os.Args[1:]
	offsetPtr := flag.Int("o", 0, "offset for get query")
	limitPtr := flag.Int("l", 1, "limit for get query")
	flag.Parse()
	if len(flag.Args()) > 0 {
		user, err := getByName(flag.Args()[0])
		if err != nil {
			fmt.Printf("Error querying for data: %s", err.Error())
			os.Exit(1)
		}
		fmt.Printf("\n%s", user.String())
		return
	}
	users, err := getUsers(*offsetPtr, *limitPtr)
	if err != nil {
		fmt.Printf("Error querying for data: %s", err.Error())
		os.Exit(1)
	}
	for _, user := range users {
		fmt.Printf("\n%s", user.String())
	}
}

func add() {
	os.Args = os.Args[2:]
	if len(os.Args) == 0 {
		fmt.Printf("Error: provide at least 1 user name")
		os.Exit(1)
	}
	newUsers, err := addManyUsers(os.Args)
	if err != nil {
		fmt.Printf("Error adding users: %s", err.Error())
		os.Exit(1)
	}
	fmt.Printf("\nAdded user's IDs:\n")
	for _, user := range newUsers {
		fmt.Printf("%d ", user.ID)
	}
}

func edit() {
	os.Args = os.Args[2:]
	if len(os.Args) < 2 {
		fmt.Printf("Error: provide user id and new user name")
		os.Exit(1)
	}
	id, err := strconv.Atoi(os.Args[0])
	if err != nil || id < 1 {
		fmt.Printf("Error: user id must be a positive integer")
		os.Exit(1)
	}
	user, err := editUser(id, os.Args[1])
	if err != nil {
		fmt.Printf("Error editing user: %s", err.Error())
		os.Exit(1)
	}
	fmt.Printf("Edited user:\n%s", user.String())
}

func delete() {
	os.Args = os.Args[2:]
	if len(os.Args) < 1 {
		fmt.Printf("Error: provide user id")
		os.Exit(1)
	}
	id, err := strconv.Atoi(os.Args[0])
	if err != nil || id < 1 {
		fmt.Printf("Error: user id must be a positive integer")
		os.Exit(1)
	}
	user, err := deleteUser(id)
	if err != nil {
		fmt.Printf("Error deleting user: %s", err.Error())
		os.Exit(1)
	}
	fmt.Printf("Deleted user:\n%s", user.String())
}
