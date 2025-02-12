package main

import (
	"errors"

	"github.com/tebeka/selenium"
)

var errWrongCredentials = errors.New("wrong credentials")

type LoginPage struct {
	wb selenium.WebDriver
}

func NewLoginPage(driver selenium.WebDriver) (*LoginPage, error) {
	err := driver.Get("https://www.knskrsk.ru/service.html")
	if err != nil {
		return nil, err
	}
	return &LoginPage{
		wb: driver,
	}, nil
}

func (l *LoginPage) EnterEmail(email string) error {
	loginField, err := l.wb.FindElement(selenium.ByCSSSelector, "#login")
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
	passwordField, err := l.wb.FindElement(selenium.ByCSSSelector, "#pass")
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
	submitBtn, err := l.wb.FindElement(selenium.ByCSSSelector, ".btn-primary")
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
	l.wb.SetImplicitWaitTimeout(0)
	err = l.wb.WaitWithTimeout(func(wb selenium.WebDriver) (bool, error) {
		alert, err := l.wb.FindElement(selenium.ByCSSSelector, ".alert-danger")
		if err == nil {
			alertText, err := alert.Text()
			if err == nil && alertText == "Не верно указан логин или пароль" {
				return true, errWrongCredentials
			}
		}
		_, err = l.wb.FindElement(selenium.ByLinkText, "Выйти из системы")
		if err == nil {
			return true, nil
		}
		return false, nil
	}, waitTime)
	l.wb.SetImplicitWaitTimeout(waitTime)
	if err != nil {
		return err
	}
	return nil
}
