package main

import (
	"testing"
)

func TestLogin(t *testing.T) {
	t.Parallel()
	service, driver, err := NewDriver()
	if err != nil {
		t.Fatalf("error starting service or driver: %v", err)
	}
	t.Cleanup(func() {
		driver.Quit()
		service.Stop()
	})

	loginPage, err := NewLoginPage(driver)
	if err != nil {
		t.Fatalf("error getting webpage: %v\n", err)
	}

	email, password, err := GetCredentials()
	if err != nil {
		t.Fatalf("error getting credentials: %v\n", err)
	}
	tests := map[string]struct {
		email    string
		password string
		wantErr  bool
	}{
		"invalid credentials": {email, password + "1", true},
		"valid credentials":   {email, password, false},
	}
	for testName, tt := range tests {
		t.Run(testName, func(t *testing.T) {
			err = loginPage.Login(tt.email, tt.password)
			if err == nil && tt.wantErr {
				t.Fatalf("expected an error")
			} else if err != nil && !tt.wantErr {
				t.Fatalf("unexpected error: %v", err)
			}
		})
	}
}
