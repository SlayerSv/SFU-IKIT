package main

import (
	"database/sql"
	"encoding/json"
	"errors"
	"fmt"
	"net/http"
	"strconv"
)

var errNotfound = errors.New("not found")
var errInternal = errors.New("internal server error")
var errBadRequest = errors.New("bad request")

func get(w http.ResponseWriter, r *http.Request) {
	offsetStr := r.URL.Query().Get("offset")
	offset, err := strconv.Atoi(offsetStr)
	if err != nil {
		errorJSON(w, r, fmt.Errorf("%w\nincorrect offset query param value\n%w",
			errBadRequest, err))
		return
	}
	limitStr := r.URL.Query().Get("limit")
	limit, err := strconv.Atoi(limitStr)
	if err != nil {
		errorJSON(w, r, fmt.Errorf("%w\nincorrect limit query param value\n%w",
			errBadRequest, err))
		return
	}
	if limit == 0 {
		limit = 1
	}
	users, err := getUsers(offset, limit)
	if err != nil {
		errorJSON(w, r, errors.Join(errInternal, err))
		return
	}
	if len(users) == 0 {
		errorJSON(w, r, errNotfound)
		return
	}
	encode(w, r, users)
}

func getByID(w http.ResponseWriter, r *http.Request) {
	stringId := r.PathValue("id")
	id, err := strconv.Atoi(stringId)
	if err != nil || id < 1 {
		errorJSON(w, r, fmt.Errorf("%w\nincorrect id value\n%w", errBadRequest, err))
		return
	}
	users, err := getUserByID(id)
	if err != nil {
		errorJSON(w, r, errors.Join(errInternal, err))
		return
	}
	encode(w, r, users)
}

func add(w http.ResponseWriter, r *http.Request) {
	decoder := json.NewDecoder(r.Body)
	user := User{}
	err := decoder.Decode(&user)
	if err != nil {
		errorJSON(w, r, errors.Join(errBadRequest, err))
		return
	}
	addedUser, err := addUser(user)
	if err != nil {
		errorJSON(w, r, errors.Join(errInternal, err))
		return
	}
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(201)
	encode(w, r, addedUser)
}

func edit(w http.ResponseWriter, r *http.Request) {
	decoder := json.NewDecoder(r.Body)
	user := User{}
	err := decoder.Decode(&user)
	if err != nil {
		errorJSON(w, r, errors.Join(errBadRequest, err))
		return
	}
	editedUser, err := editUser(user)
	if err != nil {
		errorJSON(w, r, errors.Join(errInternal, err))
		return
	}
	encode(w, r, editedUser)
}

func delete(w http.ResponseWriter, r *http.Request) {
	stringId := r.PathValue("id")
	id, err := strconv.Atoi(stringId)
	if err != nil || id < 1 {
		errorJSON(w, r, fmt.Errorf("%w\nincorrect id value\n%w", errBadRequest, err))
		return
	}
	deletededUser, err := deleteUser(id)
	if err != nil {
		errorJSON(w, r, errors.Join(errInternal, err))
		return
	}
	encode(w, r, deletededUser)
}

func encode(w http.ResponseWriter, r *http.Request, obj interface{}) {
	w.Header().Set("Content-Type", "application/json")
	encoder := json.NewEncoder(w)
	encoder.SetIndent("", "  ")
	err := encoder.Encode(obj)
	if err != nil {
		errorJSON(w, r, err)
	}
}

func errorJSON(w http.ResponseWriter, r *http.Request, err error) {
	w.Header().Set("Content-Type", "application/json")
	w.Header().Set("X-Content-Type-Options", "nosniff")
	fmt.Println(r.Method, r.URL, err.Error())
	var code int
	if errors.Is(err, sql.ErrNoRows) {
		code = http.StatusNotFound
		err = errNotfound
	} else if errors.Is(err, errBadRequest) {
		code = http.StatusBadRequest
	} else {
		code = http.StatusInternalServerError
	}
	w.WriteHeader(code)
	json.NewEncoder(w).Encode(struct {
		Error string `json:"error"`
	}{
		Error: err.Error(),
	})
}
