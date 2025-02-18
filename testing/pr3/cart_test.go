package main

import (
	"os"
	"testing"
	"time"

	"github.com/tebeka/selenium"
	"github.com/tebeka/selenium/chrome"
)

func TestCart(t *testing.T) {
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
	items, err := searchPage.Search("RTX4060")
	if err != nil {
		t.Fatalf("%v", err)
	}
	WaitForPageLoad(searchPage.wd)

	// test add item to cart
	items[0].AddToCart()
	WaitForPageLoad(searchPage.wd)
	// close pop-up window
	closeBtn, err := searchPage.wd.FindElement(selenium.ByCSSSelector, ".close")
	if err == nil {
		closeBtn.Click()
	}
	// website backend sometimes cant process adding item fast enough
	time.Sleep(time.Millisecond * 100)
	cartPage, err := NewCartPage(searchPage.wd)
	if err != nil {
		t.Fatalf("error getting webpage: %v\n", err)
	}
	cartItems, err := cartPage.GetCartItems()
	if err != nil {
		t.Fatal(err)
	}
	if len(cartItems) != 1 {
		scr, _ := driver.Screenshot()
		os.WriteFile("screen.jpg", scr, 0644)
		t.Fatalf("expected exactly 1 item in cart, got %d", len(cartItems))
	}

	// test set count
	cartItem := cartItems[0]
	newCount := 3
	err = cartItem.SetCount(newCount)
	if err != nil {
		t.Fatal(err)
	}
	cartPage, err = NewCartPage(cartPage.wd)
	if err != nil {
		t.Fatalf("error getting webpage: %v\n", err)
	}
	cartItems, err = cartPage.GetCartItems()
	if err != nil {
		t.Fatal(err)
	}
	if len(cartItems) != 1 {
		t.Fatalf("expected exactly 1 item in cart, got %d", len(cartItems))
	}
	cartItem = cartItems[0]
	currCount, err := cartItem.GetCount()
	if err != nil {
		t.Fatal(err)
	}
	if currCount != newCount {
		t.Errorf("Wrong count got %d want %d", currCount, newCount)
	}

	// test increment
	prevCount := currCount
	err = cartItem.IncrementCount()
	if err != nil {
		t.Fatal(err)
	}
	cartPage, err = NewCartPage(cartPage.wd)
	if err != nil {
		t.Fatalf("error getting webpage: %v\n", err)
	}
	cartItems, err = cartPage.GetCartItems()
	if err != nil {
		t.Fatal(err)
	}
	if len(cartItems) != 1 {
		t.Fatalf("expected exactly 1 item in cart, got %d", len(cartItems))
	}
	cartItem = cartItems[0]
	currCount, err = cartItem.GetCount()
	if err != nil {
		t.Fatal(err)
	}
	if currCount != prevCount+1 {
		t.Errorf("Wrong count got %d want %d", currCount, prevCount+1)
	}

	// test decrement
	prevCount = currCount
	err = cartItem.DecrementCount()
	if err != nil {
		t.Fatal(err)
	}
	cartPage, err = NewCartPage(cartPage.wd)
	if err != nil {
		t.Fatalf("error getting webpage: %v\n", err)
	}
	cartItems, err = cartPage.GetCartItems()
	if err != nil {
		t.Fatal(err)
	}
	if len(cartItems) != 1 {
		t.Fatalf("expected exactly 1 item in cart, got %d", len(cartItems))
	}
	cartItem = cartItems[0]
	currCount, err = cartItem.GetCount()
	if err != nil {
		t.Fatal(err)
	}
	if currCount != prevCount-1 {
		t.Errorf("Wrong count got %d want %d", currCount, prevCount-1)
	}

	// test delete
	err = cartItem.Delete()
	if err != nil {
		t.Fatal(err)
	}
	WaitForPageLoad(cartPage.wd)
	cartItems, err = cartPage.GetCartItems()
	if err != nil {
		t.Fatal(err)
	}
	if len(cartItems) != 0 {
		t.Fatal("expected cart to be empty")
	}
}
