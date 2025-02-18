package main

import (
	"fmt"

	"strings"

	"github.com/tebeka/selenium"
)

type SearchPage struct {
	wd selenium.WebDriver
}

func NewSearchPage(driver selenium.WebDriver) (*SearchPage, error) {
	err := driver.Get("https://www.knskrsk.ru")
	if err != nil {
		return nil, err
	}
	WaitForPageLoad(driver)
	return &SearchPage{
		wd: driver,
	}, nil
}

func (s *SearchPage) Search(itemName string) ([]Item, error) {
	err := s.EnterItemName(itemName)
	if err != nil {
		return nil, err
	}
	err = s.ClickSearchBtn()
	if err != nil {
		return nil, err
	}
	alert, err := s.wd.FindElement(selenium.ByCSSSelector, ".alert-danger")
	if err == nil {
		alertText, err := alert.Text()
		if err == nil && strings.Contains(alertText, "не найдено товаров") {
			return []Item{}, nil
		}
	}
	items, err := s.GetItems()
	if err != nil {
		return nil, err
	}
	return items, nil
}

func (s *SearchPage) EnterItemName(itemName string) error {
	input, err := s.wd.FindElement(selenium.ByCSSSelector, ".form-control")
	if err != nil {
		return fmt.Errorf("error finding search input field: %v", err)
	}
	err = input.Clear()
	if err != nil {
		return fmt.Errorf("error clearing search input field: %v", err)
	}
	err = input.SendKeys(itemName)
	if err != nil {
		return fmt.Errorf("error sending text to search field: %v", err)
	}
	return nil
}

func (s *SearchPage) ClickSearchBtn() error {
	searchBtn, err := s.wd.FindElement(selenium.ByCSSSelector, ".input-group-append")
	if err != nil {
		return fmt.Errorf("error finding search button: %v", err)
	}
	err = searchBtn.Click()
	if err != nil {
		return fmt.Errorf("error clicking search button: %v", err)
	}
	return nil
}

func (s *SearchPage) GetItems() ([]Item, error) {
	elements, err := s.wd.FindElements(selenium.ByCSSSelector, ".item")
	if err != nil {
		return nil, fmt.Errorf("error finding items: %v", err)
	}
	products := make([]Item, 0, len(elements))
	for _, el := range elements {
		products = append(products, NewItem(el))
	}
	return products, nil
}
