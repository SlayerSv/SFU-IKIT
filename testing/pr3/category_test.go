package main

import (
	"fmt"
	"testing"
)

func TestCategory(t *testing.T) {
	t.Parallel()
	service, driver, err := NewDriver()
	if err != nil {
		t.Fatalf("error starting service or driver: %v", err)
	}
	t.Cleanup(func() {
		driver.Quit()
		service.Stop()
	})

	categoryPage, err := NewCategoryPage(driver)
	if err != nil {
		t.Fatalf("error getting webpage: %v\n", err)
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
