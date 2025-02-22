package main

import (
	"fmt"
	"testing"
	"time"

	"github.com/tebeka/selenium"
	"github.com/tebeka/selenium/chrome"
)

func TestCart(t *testing.T) {
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
	err = WaitForPageLoad(searchPage.wd)
	if err != nil {
		t.Fatalf("%v", err)
	}
	// test add item to cart
	items[0].AddToCart()
	if err != nil {
		t.Fatalf("%v", err)
	}
	// close pop-up window
	err = searchPage.wd.WaitWithTimeout(func(wd selenium.WebDriver) (bool, error) {
		closeBtn, err := wd.FindElement(selenium.ByCSSSelector, ".close")
		if err == nil {
			closeBtn.Click()
			return true, nil
		}
		return false, err
	}, timeout)
	if err != nil {
		t.Fatalf("error finding popup closing button after adding item to a cart")
	}
	cartPage, err := NewCartPage(searchPage.wd)
	if err != nil {
		t.Fatalf("error getting webpage: %v\n", err)
	}
	cartItems, err := cartPage.GetCartItems()
	if err != nil {
		t.Fatal(err)
	}
	if len(cartItems) != 1 {
		TakeScreenshot(searchPage.wd)
		t.Fatalf("expected exactly 1 item in cart, got %d", len(cartItems))
	}

	// test set count
	cartItem := cartItems[0]
	newCount := 3
	err = cartItem.SetCount(newCount)
	if err != nil {
		t.Fatal(err)
	}
	time.Sleep(time.Millisecond * 1000)
	cartItems, err = cartPage.GetCartItems()
	if err != nil {
		t.Fatal(err)
	}
	if len(cartItems) != 1 {
		TakeScreenshot(cartPage.wd)
		t.Fatalf("expected exactly 1 item in cart, got %d", len(cartItems))
	}
	cartItem = cartItems[0]
	currCount, err := cartItem.GetCount()
	if err != nil {
		t.Fatal(err)
	}
	if currCount != newCount {
		TakeScreenshot(cartPage.wd)
		t.Fatalf("Wrong count got %d want %d", currCount, newCount)
	}

	// test increment
	prevCount := currCount
	err = cartItem.IncrementCount()
	if err != nil {
		t.Fatal(err)
	}
	time.Sleep(time.Millisecond * 1000)
	cartItems, err = cartPage.GetCartItems()
	if err != nil {
		t.Fatal(err)
	}
	if len(cartItems) != 1 {
		TakeScreenshot(searchPage.wd)
		t.Fatalf("expected exactly 1 item in cart, got %d", len(cartItems))
	}
	cartItem = cartItems[0]
	currCount, err = cartItem.GetCount()
	if err != nil {
		t.Fatal(err)
	}
	if currCount != prevCount+1 {
		TakeScreenshot(searchPage.wd)
		t.Fatalf("Wrong count got %d want %d", currCount, prevCount+1)
	}

	// test decrement
	prevCount = currCount
	err = cartItem.DecrementCount()
	if err != nil {
		TakeScreenshot(searchPage.wd)
		t.Fatal(err)
	}
	time.Sleep(time.Millisecond * 1000)
	cartItems, err = cartPage.GetCartItems()
	if err != nil {
		t.Fatal(err)
	}
	if len(cartItems) != 1 {
		TakeScreenshot(searchPage.wd)
		t.Fatalf("expected exactly 1 item in cart, got %d", len(cartItems))
	}
	cartItem = cartItems[0]
	currCount, err = cartItem.GetCount()
	if err != nil {
		t.Fatal(err)
	}
	if currCount != prevCount-1 {
		TakeScreenshot(searchPage.wd)
		t.Fatalf("Wrong count got %d want %d", currCount, prevCount-1)
	}

	// test delete
	err = cartItem.Delete()
	if err != nil {
		t.Fatal(err)
	}
	time.Sleep(time.Millisecond * 1000)
	cartItems, err = cartPage.GetCartItems()
	if err != nil {
		t.Fatal(err)
	}
	if len(cartItems) != 0 {
		TakeScreenshot(searchPage.wd)
		t.Fatal("expected cart to be empty")
	}
}
