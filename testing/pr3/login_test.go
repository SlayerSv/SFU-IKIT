package main

import (
	"testing"

	"github.com/tebeka/selenium"
	"github.com/tebeka/selenium/chrome"
)

func TestLogin(t *testing.T) {
	service, err := selenium.NewChromeDriverService("./chromedriver", 4444)
	if err != nil {
		t.Fatalf("error starting service: %v\n", err)
	}
	defer service.Stop()

	caps := selenium.Capabilities{}
	caps.AddChrome(chrome.Capabilities{Args: []string{
		"--headless", "--disable-gpu", "--window-size=1920,1080",
	}})

	// create a new remote client with the specified options
	driver, err := selenium.NewRemote(caps, "")
	if err != nil {
		t.Fatalf("error getting driver: %v\n", err)
		return
	}
	defer driver.Quit()

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
				t.Errorf("expected an error %s %s", tt.email, tt.password)
			} else if err != nil && !tt.wantErr {
				t.Errorf("unexpected error: %v %s %s", err, tt.email, tt.password)
			}
		})
	}
}
