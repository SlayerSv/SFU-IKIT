package main

import (
	"testing"
	"time"
)

func TestCart(t *testing.T) {
	t.Parallel()
	service, driver, err := NewDriver()
	if err != nil {
		t.Fatalf("error starting service or driver: %v", err)
	}
	t.Cleanup(func() {
		driver.Quit()
		service.Stop()
	})
	searchPage, err := NewSearchPage(driver)
	if err != nil {
		t.Fatalf("error getting webpage: %v\n", err)
	}
	items, err := searchPage.Search("RTX4060")
	if err != nil {
		t.Fatalf("%v", err)
	}

	// test add item to cart
	searchPage.AddToCart(items[0])
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
	time.Sleep(waitTime)
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
	time.Sleep(waitTime)
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
	time.Sleep(waitTime)
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
	time.Sleep(waitTime)
	cartItems, err = cartPage.GetCartItems()
	if err != nil {
		t.Fatal(err)
	}
	if len(cartItems) != 0 {
		TakeScreenshot(searchPage.wd)
		t.Fatal("expected cart to be empty")
	}
}
