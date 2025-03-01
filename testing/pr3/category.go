package main

import (
	"fmt"
	"strconv"
	"time"

	"github.com/tebeka/selenium"
)

type CategoryPage struct {
	wd selenium.WebDriver
}

func NewCategoryPage(driver selenium.WebDriver) (*CategoryPage, error) {
	err := driver.Get("https://www.knskrsk.ru")
	if err != nil {
		return nil, err
	}
	err = WaitForPageLoad(driver)
	if err != nil {
		return nil, err
	}
	// close pop-up window
	okBtn, err := driver.FindElement(selenium.ByCSSSelector, ".city-ok")
	if err == nil {
		okBtn.Click()
	}
	return &CategoryPage{
		wd: driver,
	}, nil
}

func (c *CategoryPage) GoToCategory(category string) error {
	btn, err := c.wd.FindElement(selenium.ByCSSSelector, ".menu-toggler")
	if err == nil {
		btn.Click()
	}
	time.Sleep(waitTime / 10)
	menu, err := c.wd.FindElement(selenium.ByCSSSelector, ".menu-cat")
	if err != nil {
		return fmt.Errorf("error finding categories menu: %v", err)
	}
	cat, err := menu.FindElement(selenium.ByLinkText, category)
	if err != nil {
		return fmt.Errorf("error finding category %s: %v", category, err)
	}
	err = cat.Click()
	if err != nil {
		return fmt.Errorf("error clicking category: %v", err)
	}
	WaitForPageLoad(c.wd)
	return nil
}

func (c *CategoryPage) FilterByPrice(min, max int) error {
	minField, err := c.wd.FindElement(selenium.ByName, "s_price1")
	if err != nil {
		return fmt.Errorf("error finding min price field %v", err)
	}
	err = minField.Clear()
	if err != nil {
		return fmt.Errorf("error clearing min price field %v", err)
	}
	err = minField.SendKeys(strconv.Itoa(min))
	if err != nil {
		return fmt.Errorf("error setting min price field %v", err)
	}
	maxField, err := c.wd.FindElement(selenium.ByName, "s_price2")
	if err != nil {
		return fmt.Errorf("error finding max price field %v", err)
	}
	err = maxField.Clear()
	if err != nil {
		return fmt.Errorf("error clearing max price field %v", err)
	}
	err = maxField.SendKeys(strconv.Itoa(max))
	if err != nil {
		return fmt.Errorf("error setting max price field %v", err)
	}
	minField.SendKeys("")
	time.Sleep(waitTime)
	popup, err := c.wd.FindElement(selenium.ByID, "SearchExtPopup")
	if err != nil {
		return fmt.Errorf("error finding search popup %v", err)
	}
	link, err := popup.FindElement(selenium.ByTagName, "a")
	if err != nil {
		return fmt.Errorf("error finding search link %v", err)
	}
	err = link.Click()
	if err != nil {
		return fmt.Errorf("error clicking search link %v", err)
	}
	WaitForPageLoad(c.wd)
	return nil
}
