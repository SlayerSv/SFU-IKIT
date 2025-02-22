package main

import (
	"fmt"
	"testing"

	"github.com/tebeka/selenium"
	"github.com/tebeka/selenium/chrome"
)

func TestLogin(t *testing.T) {
	t.Parallel()
	port := GetNextPortNumber()
	service, err := selenium.NewChromeDriverService("./chromedriver", port)
	if err != nil {
		t.Fatalf("error starting service: %v\n", err)
	}
	t.Cleanup(func() {
		service.Stop()
	})

	var caps = selenium.Capabilities{}
	caps.AddChrome(chrome.Capabilities{Args: chromeArgs})

	// create a new remote client with the specified options
	driver, err := selenium.NewRemote(caps, fmt.Sprintf("http://localhost:%d/wd/hub", port))
	if err != nil {
		t.Fatalf("error getting driver: %v\n", err)
		return
	}
	t.Cleanup(func() {
		driver.Quit()
	})

	loginPage, err := NewLoginPage(driver)
	if err != nil {
		t.Fatalf("error getting webpage: %v\n", err)
	}
	// close pop-up window
	okBtn, err := driver.FindElement(selenium.ByCSSSelector, ".city-ok")
	if err == nil {
		okBtn.Click()
	}
	email, password, err := GetCredentials()
	if err != nil {
		t.Fatalf("error getting credentials: %v\n", err)
	}
	tests := map[string]struct {
		email    string
		password string
		wantErr  bool
	}{
		"invalid credentials": {email, password + "1", true},
		"valid credentials":   {email, password, false},
	}
	for testName, tt := range tests {
		t.Run(testName, func(t *testing.T) {
			err = loginPage.Login(tt.email, tt.password)
			if err == nil && tt.wantErr {
				t.Fatalf("expected an error")
			} else if err != nil && !tt.wantErr {
				t.Fatalf("unexpected error: %v", err)
			}
		})
	}
}
