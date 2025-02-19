package main

import (
	"fmt"
	"os"
	"strings"

	"github.com/tebeka/selenium"
)

var errWrongCredentials = fmt.Errorf("wrong credentials")

type LoginPage struct {
	wd selenium.WebDriver
}

func NewLoginPage(driver selenium.WebDriver) (*LoginPage, error) {
	err := driver.Get("https://www.knskrsk.ru/service.html")
	if err != nil {
		return nil, err
	}
	WaitForPageLoad(driver)
	return &LoginPage{
		wd: driver,
	}, nil
}

func (l *LoginPage) EnterEmail(email string) error {
	loginField, err := l.wd.FindElement(selenium.ByCSSSelector, "#login")
	if err != nil {
		return err
	}
	err = loginField.Clear()
	if err != nil {
		return err
	}
	err = loginField.SendKeys(email)
	if err != nil {
		return err
	}
	return nil
}

func (l *LoginPage) EnterPassword(password string) error {
	passwordField, err := l.wd.FindElement(selenium.ByCSSSelector, "#pass")
	if err != nil {
		return err
	}
	err = passwordField.Clear()
	if err != nil {
		return err
	}
	err = passwordField.SendKeys(password)
	if err != nil {
		return err
	}
	return nil
}

func (l *LoginPage) Submit() error {
	submitBtn, err := l.wd.FindElement(selenium.ByCSSSelector, ".btn-primary")
	if err != nil {
		return err
	}
	err = submitBtn.Click()
	if err != nil {
		return err
	}
	return nil
}

func (l *LoginPage) Login(email, password string) error {
	err := l.EnterEmail(email)
	if err != nil {
		return err
	}
	err = l.EnterPassword(password)
	if err != nil {
		return err
	}
	err = l.Submit()
	if err != nil {
		return err
	}
	WaitForPageLoad(l.wd)
	alert, err := l.wd.FindElement(selenium.ByCSSSelector, ".alert-danger")
	if err == nil {
		alertText, err := alert.Text()
		if err == nil && alertText == "Не верно указан логин или пароль" {
			return errWrongCredentials
		}
	}
	return nil
}

func GetCredentials() (string, string, error) {
	credentials, err := os.ReadFile("credentials.txt")
	if err != nil {
		return "", "", fmt.Errorf("error reading credentials file: %v", err)
	}
	creds := strings.Fields(string(credentials))
	if len(creds) != 2 {
		return "", "", fmt.Errorf("credentials must have 2 values, have: %d", len(creds))
	}
	return creds[0], creds[1], nil
}
