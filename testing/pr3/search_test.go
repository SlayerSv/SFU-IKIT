package main

import (
	"testing"

	"github.com/tebeka/selenium"
	"github.com/tebeka/selenium/chrome"
)

func TestSearch(t *testing.T) {
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
