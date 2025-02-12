package main

import (
	"fmt"
	"os"

	//"strings"
	"time"

	"github.com/tebeka/selenium"
	"github.com/tebeka/selenium/chrome"
)

const (
	waitTime = time.Second * 2
)

func main() {
	service, err := selenium.NewChromeDriverService("./chromedriver", 4444)
	if err != nil {
		fmt.Println(err)
		return
	}
	defer service.Stop()

	caps := selenium.Capabilities{}
	caps.AddChrome(chrome.Capabilities{Args: []string{
		"--headless", "--disable-gpu", "--window-size=1920,1080",
	}})

	// create a new remote client with the specified options
	driver, err := selenium.NewRemote(caps, "")
	if err != nil {
		fmt.Println(err)
		return
	}
	defer driver.Quit()

	driver.SetImplicitWaitTimeout(waitTime)
	err = driver.Get("https://www.knskrsk.ru")
	if err != nil {
		fmt.Printf("Error getting webpage: %v\n", err)
	}

	// close pop-up window
	okBtn, err := driver.FindElement(selenium.ByCSSSelector, ".city-ok")
	if err == nil {
		okBtn.Click()
	}

	loginPage, err := NewLoginPage(driver)
	email, err := os.ReadFile("email.txt")
	if err != nil {
		fmt.Printf("Error reading email from file: %v\n", err)
		return
	}
	password, err := os.ReadFile("password.txt")
	if err != nil {
		fmt.Printf("Error reading password from file: %v\n", err)
		return
	}

	err = loginPage.Login(string(email), string(password))
	if err != nil {
		fmt.Println(err)
	}
	scr, err := driver.Screenshot()
	os.WriteFile("screen.jpg", scr, 0644)
}
