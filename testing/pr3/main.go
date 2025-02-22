package main

import (
	"fmt"
	"os"
	"sync/atomic"
	"time"

	"github.com/tebeka/selenium"
)

const (
	timeout = time.Second * 10
)

var nextPortNumber int64 = 4440
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

func main() {

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
