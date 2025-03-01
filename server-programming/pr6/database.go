package main

import (
	"database/sql"
	"fmt"
	"os"
	"time"

	_ "github.com/lib/pq"
)

var db *sql.DB

func getUsers(offset, limit int) ([]User, error) {
	rows, err := db.Query("SELECT * FROM users ORDER BY id OFFSET $1 LIMIT $2",
		offset, limit)
	if err != nil {
		return nil, err
	}
	defer rows.Close()
	users := []User{}
	for rows.Next() {
		user := User{}
		err := rows.Scan(&user.ID, &user.Name, &user.Created_at)
		if err != nil {
			return nil, err
		}
		users = append(users, user)
	}
	if len(users) == 0 {
		return nil, sql.ErrNoRows
	}
	return users, nil
}

func getUserByID(id int) (User, error) {
	row := db.QueryRow("select * from users where id = $1", id)
	user := User{}
	err := row.Scan(&user.ID, &user.Name, &user.Created_at)
	return user, err
}

func addUser(user User) (User, error) {
	row := db.QueryRow("Insert into users(name, created_at) values ($1, $2)  returning *",
		user.Name, time.Now().Format("2006-01-02 15:04:05"))
	newUser := User{}
	err := row.Scan(&newUser.ID, &newUser.Name, &newUser.Created_at)
	if err != nil {
		return newUser, err
	}
	return newUser, nil
}

func editUser(user User) (User, error) {
	row := db.QueryRow("Update users set name = $1 where id = $2 returning *",
		user.Name, user.ID)
	editedUser := User{}
	err := row.Scan(&editedUser.ID, &editedUser.Name, &editedUser.Created_at)
	if err != nil {
		return editedUser, err
	}
	return editedUser, nil
}

func deleteUser(id int) (User, error) {
	row := db.QueryRow("Delete from users where id = $1 returning *", id)
	deletedUser := User{}
	err := row.Scan(&deletedUser.ID, &deletedUser.Name, &deletedUser.Created_at)
	if err != nil {
		return deletedUser, err
	}
	resetSequence("users")
	return deletedUser, nil
}

func resetSequence(tableName string) {
	_, err := db.Exec("SELECT setval(pg_get_serial_sequence($1, 'id'), coalesce(max(id),0) + 1, false) FROM users;",
		tableName)
	if err != nil {
		fmt.Printf("error resetting sequence %s: %v\n", tableName, err)
	}
}

func init() {
	conn, err := os.ReadFile("DBConnectionString.txt")
	if err != nil {
		fmt.Printf("Error opening connecton string file: %s", err.Error())
		os.Exit(1)
	}
	db, err = sql.Open("postgres", string(conn))
	if err != nil {
		fmt.Printf("Error opening connecton to database: %s", err.Error())
		os.Exit(1)
	}
}
