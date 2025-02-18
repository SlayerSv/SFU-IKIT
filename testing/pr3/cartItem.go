package main

import (
	"fmt"
	"strconv"

	"github.com/tebeka/selenium"
)

type CartItem struct {
	selenium.WebElement
}

func NewCartItem(element selenium.WebElement) CartItem {
	return CartItem{element}
}

func (item CartItem) String() string {
	invalid := "Invalid item\n"
	name, err := item.GetName()
	if err != nil {
		return invalid
	}
	count, err := item.GetCount()
	if err != nil {
		return invalid
	}
	price, err := item.GetPrice()
	if err != nil {
		return invalid
	}
	return fmt.Sprintf("Name: %s\nCount: %d\nPrice: %d\n",
		name, count, price,
	)
}

func (item CartItem) GetName() (string, error) {
	name, err := item.WebElement.GetAttribute("data-name")
	if err != nil {
		return "", fmt.Errorf("error getting name: %v", err)
	}
	return name, nil
}

func (item CartItem) GetPrice() (int, error) {
	priceString, err := item.WebElement.GetAttribute("data-price")
	if err != nil {
		return -1, fmt.Errorf("error getting price: %v", err)
	}
	price, err := strconv.Atoi(priceString)
	if err != nil {
		return -1, fmt.Errorf("error converting price: %v", err)
	}
	return price, nil
}

func (item CartItem) GetCount() (int, error) {
	countString, err := item.WebElement.GetAttribute("data-count")
	if err != nil {
		return -1, fmt.Errorf("error getting count: %v", err)
	}
	count, err := strconv.Atoi(countString)
	if err != nil {
		return -1, fmt.Errorf("error converting count: %v", err)
	}
	return count, nil
}

func (item CartItem) SetCount(count int) error {
	if count < 1 {
		return fmt.Errorf("count cannot be less than 1")
	}
	countInputField, err := item.FindElement(selenium.ByCSSSelector, ".form-control")
	if err != nil {
		return fmt.Errorf("error getting count input field: %v", err)
	}
	err = countInputField.Clear()
	if err != nil {
		return fmt.Errorf("error clearing count input field: %v", err)
	}
	err = countInputField.SendKeys(strconv.Itoa(count))
	if err != nil {
		return fmt.Errorf("error setting count input field: %v", err)
	}
	return nil
}

func (item CartItem) IncrementCount() error {
	inc, err := item.FindElement(selenium.ByCSSSelector, ".quan-plus")
	if err != nil {
		return fmt.Errorf("error finding increment button: %v", err)
	}
	err = inc.Click()
	if err != nil {
		return fmt.Errorf("error clicking increment button: %v", err)
	}
	return nil
}

func (item CartItem) DecrementCount() error {
	dec, err := item.FindElement(selenium.ByCSSSelector, ".quan-minus")
	if err != nil {
		return fmt.Errorf("error finding decrement button: %v", err)
	}
	err = dec.Click()
	if err != nil {
		return fmt.Errorf("error clicking decrement button: %v", err)
	}
	return nil
}

func (item CartItem) Delete() error {
	deleteBtn, err := item.FindElement(selenium.ByCSSSelector, ".text-nounderline")
	if err != nil {
		return fmt.Errorf("error finding delete button: %v", err)
	}
	err = deleteBtn.Click()
	if err != nil {
		return fmt.Errorf("error clicking delete button: %v", err)
	}
	return nil
}
