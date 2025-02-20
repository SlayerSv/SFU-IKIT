package main

import (
	"fmt"
	"testing"

	"github.com/tebeka/selenium"
	"github.com/tebeka/selenium/chrome"
)

func TestSearch(t *testing.T) {
	t.Parallel()
	port := 4440
	service, err := selenium.NewChromeDriverService("./chromedriver", port)
	if err != nil {
		t.Fatalf("error starting service: %v\n", err)
	}
	t.Cleanup(func() {
		service.Stop()
	})

	caps := selenium.Capabilities{}
	caps.AddChrome(chrome.Capabilities{Args: []string{
		"--headless",
		"--disable-gpu",
		"--window-size=1920,1080",
		"--use-fake-ui-for-media-stream",
		"--disable-bluetooth",
		"--disable-device-discovery-notifications",
		"--disable-hid-blocklist",
		"--log-level=3",
	}})

	// create a new remote client with the specified options
	driver, err := selenium.NewRemote(caps, fmt.Sprintf("http://localhost:%d/wd/hub", port))
	if err != nil {
		t.Fatalf("error getting driver: %v\n", err)
		return
	}
	t.Cleanup(func() {
		driver.Quit()
	})

	searchPage, err := NewSearchPage(driver)
	if err != nil {
		t.Fatalf("error getting webpage: %v\n", err)
	}
	// close pop-up window
	okBtn, err := driver.FindElement(selenium.ByCSSSelector, ".city-ok")
	if err == nil {
		okBtn.Click()
	}
	tests := []struct {
		itemName  string
		wantEmpty bool
	}{
		{"RTX5040", true},
		{"RTX4060", false},
	}
	for _, tt := range tests {
		t.Run(tt.itemName, func(t *testing.T) {
			items, err := searchPage.Search(tt.itemName)
			if err != nil {
				t.Fatalf("unexpected error: %v", err)
			}
			if len(items) == 0 && !tt.wantEmpty {
				t.Fatalf("expected to find items")
			} else if len(items) != 0 && tt.wantEmpty {
				t.Fatalf("expected to not find any items %s", items)
			}
		})
	}
}
