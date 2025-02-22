package main

import (
	"fmt"
	"testing"

	"github.com/tebeka/selenium"
	"github.com/tebeka/selenium/chrome"
)

func TestCategory(t *testing.T) {
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
