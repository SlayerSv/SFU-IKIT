package main

import (
	"fmt"
	"strconv"

	"github.com/tebeka/selenium"
)

type Item struct {
	selenium.WebElement
}

func NewItem(element selenium.WebElement) Item {
	return Item{element}
}

func (item Item) String() string {
	invalid := "Invalid item\n"
	name, err := item.GetName()
	if err != nil {
		return invalid
	}
	desc, err := item.GetDescription()
	if err != nil {
		return invalid
	}
	price, err := item.GetPrice()
	if err != nil {
		price = "Out of stock"
	}
	return fmt.Sprintf("Name: %s\nDescription: %s\nPrice: %s\n",
		name, desc, price,
	)
}

func (item Item) GetName() (string, error) {
	div, err := item.FindElement(selenium.ByCSSSelector, ".name")
	if err != nil {
		return "", fmt.Errorf("error getting div name: %v", err)
	}
	span, err := div.FindElement(selenium.ByTagName, "span")
	if err != nil {
		return "", fmt.Errorf("error getting span name: %v", err)
	}
	name, err := span.Text()
	if err != nil {
		return "", fmt.Errorf("error getting span name text: %v", err)
	}
	return name, nil
}

func (item Item) GetDescription() (string, error) {
	div, err := item.FindElement(selenium.ByCSSSelector, ".goods-annt")
	if err != nil {
		return "", fmt.Errorf("error getting div desc: %v", err)
	}
	description, err := div.Text()
	if err != nil {
		return "", fmt.Errorf("error getting desc text: %v", err)
	}
	return description, nil
}

func (item Item) GetPrice() (string, error) {
	span, err := item.FindElement(selenium.ByCSSSelector, ".price")
	if err != nil {
		return "", fmt.Errorf("error finding price field: %v", err)
	}
	price, err := span.Text()
	if err != nil {
		return "", fmt.Errorf("error getting price text: %v", err)
	}
	return price, nil
}

func (item Item) parsePrice(priceString string) (int, error) {
	priceB := make([]byte, 0, len(priceString))
	for _, l := range priceString {
		if l <= '9' && l >= '0' {
			priceB = append(priceB, byte(l))
		}
	}
	price, err := strconv.Atoi(string(priceB))
	if err != nil {
		return -1, err
	}
	return price, nil
}
