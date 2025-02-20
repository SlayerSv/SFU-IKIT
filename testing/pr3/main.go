package main

import (
	"time"

	"github.com/tebeka/selenium"
)

const (
	waitTime = time.Second * 10
)

func main() {

}

func WaitForPageLoad(driver selenium.WebDriver) {
	driver.WaitWithTimeout(func(wd selenium.WebDriver) (bool, error) {
		script := "return document.readyState"
		state, err := wd.ExecuteScript(script, nil)
		if err != nil {
			return false, err
		}
		return state == "complete", nil
	}, waitTime)
}
