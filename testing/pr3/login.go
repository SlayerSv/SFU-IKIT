package main

import (
	"errors"
	"time"

	"github.com/tebeka/selenium"
)

var errWrongCredentials = errors.New("wrong credentials")

type LoginPage struct {
	page selenium.WebDriver
}

func NewLoginPage(driver selenium.WebDriver) (*LoginPage, error) {
	err := driver.Get("https://www.knskrsk.ru/service.html")
	if err != nil {
		return nil, err
	}
	time.Sleep(time.Second)
	return &LoginPage{
		page: driver,
	}, nil
}

func (l *LoginPage) EnterEmail(email string) error {
	loginField, err := l.page.FindElement(selenium.ByCSSSelector, "#login")
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
	passwordField, err := l.page.FindElement(selenium.ByCSSSelector, "#pass")
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
	submitBtn, err := l.page.FindElement(selenium.ByCSSSelector, ".btn-primary")
	if err != nil {
		return err
	}
	submitBtn.Click()
	time.Sleep(time.Second / 2)
	alert, err := l.page.FindElement(selenium.ByCSSSelector, ".alert-danger")
	if err == nil {
		alertText, err := alert.Text()
		if err != nil {
			return err
		}
		if alertText == "Не верно указан логин или пароль" {
			return errWrongCredentials
		}
	}
	return nil
}
