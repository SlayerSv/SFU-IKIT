package main

import (
	"flag"
	"fmt"
	"os"
)

func main() {
	if len(os.Args) < 2 {
		fmt.Println("Error: provide a command")
		os.Exit(1)
	}
	switch os.Args[1] {
	case "get":
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
	case "add":
		os.Args = os.Args[2:]
		newUsers, err := addManyUsers(os.Args)
		if err != nil {
			fmt.Printf("Error adding users: %s", err.Error())
			os.Exit(1)
		}
		fmt.Printf("\nAdded user's IDs:\n")
		for _, user := range newUsers {
			fmt.Printf("%d ", user.ID)
		}
	default:
		fmt.Println("Error: unknown command.\nAvailable commands:\nget\nadd")
		os.Exit(1)
	}

}
