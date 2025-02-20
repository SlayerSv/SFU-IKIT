package main

import (
	"fmt"
	"testing"

	"github.com/tebeka/selenium"
	"github.com/tebeka/selenium/chrome"
)

func TestCategory(t *testing.T) {
	t.Parallel()
	port := 4443
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
	categoryPage, err := NewCategoryPage(driver)
	if err != nil {
		t.Fatalf("error getting webpage: %v\n", err)
	}
	// close pop-up window
	okBtn, err := driver.FindElement(selenium.ByCSSSelector, ".city-ok")
	if err == nil {
		okBtn.Click()
	}
	categoryPage.GoToCategory("Комплектующие")
	url, err := categoryPage.wd.CurrentURL()
	if err != nil {
		t.Fatalf("error getting url: %v\n", err)
	}
	wantUrl := "https://www.knskrsk.ru/catalog/komplektuyuschie/"
	if url != wantUrl {
		t.Errorf("wrong url: got %s want %s", url, wantUrl)
	}
	minPrice, maxPrice := 3000, 10000
	err = categoryPage.FilterByPrice(minPrice, maxPrice)
	if err != nil {
		t.Error(err)
	}
	url, err = categoryPage.wd.CurrentURL()
	if err != nil {
		t.Fatalf("error getting url: %v\n", err)
	}
	wantUrl = fmt.Sprintf("https://www.knskrsk.ru/multi/catalog/komplektuyuschie/_tsena-ot-%d/_tsena-do-%d/",
		minPrice, maxPrice)
	if url != wantUrl {
		t.Errorf("wrong url: got %s want %s", url, wantUrl)
	}
}
