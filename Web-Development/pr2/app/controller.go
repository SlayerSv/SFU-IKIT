package app

import (
	"cmp"
	"database/sql"
	"encoding/json"
	"errors"
	"fmt"
	"html/template"
	"net/http"
	"net/url"
	"strconv"
	"strings"
	"unicode/utf8"

	"pr2/db"
	"pr2/models"
)

var errNotfound = errors.New("not found")
var errInternal = errors.New("internal server error")
var errBadRequest = errors.New("bad request")

func Chat(w http.ResponseWriter, r *http.Request) {
	cookie, err := r.Cookie("chat-user-name")
	if err != nil {
		http.Redirect(w, r, "/login", http.StatusSeeOther)
		return
	}
	name, err := url.QueryUnescape(cookie.Value)
	if err != nil {
		errorJSON(w, r, err)
		return
	}
	ts, err := template.ParseFiles("./ui/chat.html")
	if err != nil {
		errorJSON(w, r, err)
		return
	}
	err = ts.Execute(w, map[string]string{"name": name})
	if err != nil {
		errorJSON(w, r, err)
		return
	}
}

func LoginForm(w http.ResponseWriter, r *http.Request) {
	ts, err := template.ParseFiles("./ui/login.html")
	if err != nil {
		errorJSON(w, r, err)
		return
	}
	err = ts.Execute(w, make(map[string]string))
	if err != nil {
		errorJSON(w, r, err)
		return
	}
}

func Login(w http.ResponseWriter, r *http.Request) {
	fieldErrors := make(map[string]string)
	err := r.ParseForm()
	if err != nil {
		errorJSON(w, r, err)
		return
	}
	name := r.PostForm.Get("name")
	password := r.PostForm.Get("password")
	fieldErrors["name"] = name
	if strings.TrimSpace(name) == "" {
		fieldErrors["nameErr"] = "name cannot be empty"
	}
	if strings.TrimSpace(password) == "" {
		fieldErrors["passwordErr"] = "password cannot be empty"
	}
	if len(fieldErrors) > 1 {
		ts, err := template.ParseFiles("./ui/login.html")
		if err != nil {
			errorJSON(w, r, err)
			return
		}
		err = ts.Execute(w, fieldErrors)
		if err != nil {
			errorJSON(w, r, err)
			return
		}
		return
	}
	_, err = db.GetUser(name, password)
	if err != nil {
		fieldErrors["nameErr"] = "wrong user name or password"
		fieldErrors["passwordErr"] = "wrong user name or password"
		ts, err := template.ParseFiles("./ui/login.html")
		if err != nil {
			errorJSON(w, r, err)
			return
		}
		err = ts.Execute(w, fieldErrors)
		if err != nil {
			errorJSON(w, r, err)
			return
		}
		return
	}
	encodedName := url.QueryEscape(name)
	http.SetCookie(w, &http.Cookie{
		Name:     "chat-user-name",
		Value:    encodedName,
		HttpOnly: true,
	})
	http.Redirect(w, r, "/chat", http.StatusSeeOther)
}

func SignupForm(w http.ResponseWriter, r *http.Request) {
	ts, err := template.ParseFiles("./ui/signup.html")
	if err != nil {
		errorJSON(w, r, err)
		return
	}
	err = ts.Execute(w, make(map[string]string))
	if err != nil {
		errorJSON(w, r, err)
		return
	}
}

func Signup(w http.ResponseWriter, r *http.Request) {
	fieldErrors := make(map[string]string)
	err := r.ParseForm()
	if err != nil {
		errorJSON(w, r, err)
		return
	}
	name := r.PostForm.Get("name")
	password := r.PostForm.Get("password")
	name = strings.TrimSpace(name)
	password = strings.TrimSpace(password)
	fieldErrors["name"] = name
	if name == "" {
		fieldErrors["nameErr"] = "name cannot be empty"
	}
	if password == "" {
		fieldErrors["passwordErr"] = "password cannot be empty"
	}
	if utf8.RuneCount([]byte(name)) > 20 {
		fieldErrors["nameErr"] = "name length cannot be greater than 20"
	}
	_, err = db.GetUserByName(name)
	if err == nil {
		fieldErrors["nameErr"] = "this name is already taken"
	}
	if len(fieldErrors) > 1 {
		ts, err := template.ParseFiles("./ui/signup.html")
		if err != nil {
			errorJSON(w, r, err)
			return
		}
		err = ts.Execute(w, fieldErrors)
		if err != nil {
			errorJSON(w, r, err)
			return
		}
		return
	}
	_, err = db.AddUser(models.User{Name: name, Password: password})
	if err != nil {
		errorJSON(w, r, err)
		return
	}
	encodedName := url.QueryEscape(name)
	http.SetCookie(w, &http.Cookie{
		Name:     "chat-user-name",
		Value:    encodedName,
		HttpOnly: true,
		MaxAge:   3600,
	})
	http.Redirect(w, r, "/chat", http.StatusSeeOther)
}

func Logout(w http.ResponseWriter, r *http.Request) {
	http.SetCookie(w, &http.Cookie{
		Name:     "chat-user-name",
		Value:    "",
		HttpOnly: true,
		MaxAge:   -1,
	})
	http.Redirect(w, r, "/login", http.StatusSeeOther)
}

func Get(w http.ResponseWriter, r *http.Request) {
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
