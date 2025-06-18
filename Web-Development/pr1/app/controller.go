package app

import (
	"cmp"
	"database/sql"
	"encoding/json"
	"errors"
	"fmt"
	"html/template"
	"net/http"
	"strconv"

	"web/db"
	"web/models"
)

var errNotfound = errors.New("not found")
var errInternal = errors.New("internal server error")
var errBadRequest = errors.New("bad request")

func Metrics(w http.ResponseWriter, r *http.Request) {
	ts, err := template.ParseFiles("./ui/home.html")
	if err != nil {
		errorJSON(w, r, err)
		return
	}
	err = ts.Execute(w, nil)
	if err != nil {
		errorJSON(w, r, err)
		return
	}
}

func Get(w http.ResponseWriter, r *http.Request) {
	RequestsPerSec.Add(1)
	offsetStr := cmp.Or(r.URL.Query().Get("offset"), "0")
	offset, err := strconv.Atoi(offsetStr)
	if err != nil {
		errorJSON(w, r, fmt.Errorf("%w\nincorrect offset query param value\n%w",
			errBadRequest, err))
		return
	}
	limitStr := cmp.Or(r.URL.Query().Get("limit"), "10")
	limit, err := strconv.Atoi(limitStr)
	if err != nil {
		errorJSON(w, r, fmt.Errorf("%w\nincorrect limit query param value\n%w",
			errBadRequest, err))
		return
	}
	users, err := db.GetUsers(offset, limit)
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

func GetByID(w http.ResponseWriter, r *http.Request) {
	RequestsPerSec.Add(1)
	stringId := r.PathValue("user_id")
	id, err := strconv.Atoi(stringId)
	if err != nil || id < 1 {
		errorJSON(w, r, fmt.Errorf("%w\nincorrect id value\n%w", errBadRequest, err))
		return
	}
	users, err := db.GetUserByID(id)
	if err != nil {
		errorJSON(w, r, errors.Join(errInternal, err))
		return
	}
	encode(w, r, users)
}

func Add(w http.ResponseWriter, r *http.Request) {
	RequestsPerSec.Add(1)
	decoder := json.NewDecoder(r.Body)
	user := models.User{}
	err := decoder.Decode(&user)
	if err != nil {
		errorJSON(w, r, errors.Join(errBadRequest, err))
		return
	}
	addedUser, err := db.AddUser(user)
	if err != nil {
		errorJSON(w, r, errors.Join(errInternal, err))
		return
	}
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(201)
	encode(w, r, addedUser)
}

func Edit(w http.ResponseWriter, r *http.Request) {
	RequestsPerSec.Add(1)
	decoder := json.NewDecoder(r.Body)
	user := models.User{}
	err := decoder.Decode(&user)
	if err != nil {
		errorJSON(w, r, errors.Join(errBadRequest, err))
		return
	}
	editedUser, err := db.EditUser(user)
	if err != nil {
		errorJSON(w, r, errors.Join(errInternal, err))
		return
	}
	encode(w, r, editedUser)
}

func Delete(w http.ResponseWriter, r *http.Request) {
	RequestsPerSec.Add(1)
	stringId := r.PathValue("user_id")
	id, err := strconv.Atoi(stringId)
	if err != nil || id < 1 {
		errorJSON(w, r, fmt.Errorf("%w\nincorrect id value\n%w", errBadRequest, err))
		return
	}
	deletededUser, err := db.DeleteUser(id)
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
		Query string `json:"query"`
		Error string `json:"error"`
	}{
		Query: r.URL.String(),
		Error: err.Error(),
	})
}
