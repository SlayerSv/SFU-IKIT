package main

import (
	"fmt"

	"github.com/tebeka/selenium"
)

type CartPage struct {
	wd selenium.WebDriver
}

func NewCartPage(driver selenium.WebDriver) (*CartPage, error) {
	err := driver.Get("https://www.knskrsk.ru/basket.html")
	if err != nil {
		return nil, err
	}
	err = WaitForPageLoad(driver)
	if err != nil {
		return nil, err
	}
	return &CartPage{
		wd: driver,
	}, nil
}

func (c *CartPage) GetCartItems() ([]CartItem, error) {
	elements, err := c.wd.FindElements(selenium.ByCSSSelector, ".item")
	if err != nil {
		return nil, fmt.Errorf("error finding cart items: %v", err)
	}
	cartItems := make([]CartItem, 0, len(elements))
	for _, el := range elements {
		cartItems = append(cartItems, NewCartItem(el))
	}
	return cartItems, nil
}
