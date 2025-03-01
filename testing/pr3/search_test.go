package main

import (
	"testing"
)

func TestSearch(t *testing.T) {
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
	tests := []struct {
		itemName  string
		wantEmpty bool
	}{
		{"RTX5040", true},
		{"RTX4060", false},
	}
	for _, tt := range tests {
		t.Run(tt.itemName, func(t *testing.T) {
			items, err := searchPage.Search(tt.itemName)
			if err != nil {
				t.Fatalf("unexpected error: %v", err)
			}
			if len(items) == 0 && !tt.wantEmpty {
				t.Fatalf("expected to find items")
			} else if len(items) != 0 && tt.wantEmpty {
				t.Fatalf("expected to not find any items %s", items)
			}
		})
	}
}
