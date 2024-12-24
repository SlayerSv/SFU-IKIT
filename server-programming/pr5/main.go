package main

import (
	"fmt"
	"os"
)

var helpMsg = `
Available commands: 
get [name]
get -o [offset] -l [limit]
add [name1 name2 name3 ...]
edit [id] [new name]
delete [id]
`

func main() {
	if len(os.Args) < 2 {
		fmt.Printf("Error: provide a command.\n%s", helpMsg)
		os.Exit(1)
	}
	switch os.Args[1] {
	case "get":
		get()
	case "add":
		add()
	case "edit":
		edit()
	case "delete":
		delete()
	default:
		fmt.Printf("Error: unknown command.\n%s", helpMsg)
		os.Exit(1)
	}
}
