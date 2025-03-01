package main

import (
	"fmt"
	"os"
	"sync/atomic"
	"time"

	"github.com/tebeka/selenium"
	"github.com/tebeka/selenium/chrome"
)

const (
	timeout  = time.Second * 10
	waitTime = time.Second * 2
)

var nextPortNumber int64 = 4440

func main() {

}

func NewDriver() (*selenium.Service, selenium.WebDriver, error) {
	port := GetNextPortNumber()
	service, err := selenium.NewChromeDriverService("./chromedriver", port)
	if err != nil {
		return nil, nil, fmt.Errorf("error starting service: %v", err)
	}
	var caps = selenium.Capabilities{}
	var chromeArgs = []string{
		"--headless",
		"--disable-gpu",
		"--window-size=1920,1080",
		"--use-fake-ui-for-media-stream",
		"--disable-bluetooth",
		"--disable-device-discovery-notifications",
		"--disable-hid-blocklist",
		"--log-level=3",
	}
	caps.AddChrome(chrome.Capabilities{Args: chromeArgs})
	// create a new remote client with the specified options
	driver, err := selenium.NewRemote(caps, fmt.Sprintf("http://localhost:%d/wd/hub", port))
	if err != nil {
		return nil, nil, fmt.Errorf("error getting driver: %v", err)
	}
	return service, driver, nil
}

func GetNextPortNumber() int {
	return int(atomic.AddInt64(&nextPortNumber, 1))
}

func WaitForPageLoad(driver selenium.WebDriver) error {
	return driver.WaitWithTimeout(func(wd selenium.WebDriver) (bool, error) {
		script := "return document.readyState"
		state, err := wd.ExecuteScript(script, nil)
		if err != nil {
			return false, err
		}
		return state == "complete", nil
	}, timeout)
}

// for debugging
func TakeScreenshot(wd selenium.WebDriver) {
	scr, err := wd.Screenshot()
	if err != nil {
		fmt.Printf("error taking screenshot: %v", err)
		return
	}
	os.WriteFile("error.jpg", scr, 0644)
}
