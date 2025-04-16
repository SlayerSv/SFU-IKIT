package main

import (
	"database/sql"
	"fmt"

	_ "github.com/lib/pq"
)

type PostgresDB struct {
	*sql.DB
}

func NewPostgres(conn string) (*PostgresDB, error) {
	db, err := sql.Open("postgres", conn)
	if err != nil {
		return nil, err
	}
	p := &PostgresDB{db}
	err = p.SetupDatabase()
	if err != nil {
		return nil, err
	}
	return p, nil
}

func (db *PostgresDB) SetupDatabase() error {
	createTableSQL := `
		DROP TABLE IF EXISTS currencies;
		CREATE TABLE currencies (
			code text PRIMARY KEY,
			name text NOT NULL,
			name_plural text NOT NULL,
			symbol text NOT NULL,
			symbol_native text NOT NULL
		);
	`
	_, err := db.Exec(createTableSQL)
	if err != nil {
		return fmt.Errorf("create currencies table: %w", err)
	}
	return nil
}

// insertRecords inserts exchange rate records into the database
func (db *PostgresDB) InsertCurrencies(currencies map[string]Currency) error {
	insertSQL := `
		INSERT INTO currencies
		VALUES ($1, $2, $3, $4, $5)
	`
	for name, currency := range currencies {
		_, err := db.Exec(insertSQL,
			currency.Code,
			currency.Name,
			currency.NamePlural,
			currency.Symbol,
			currency.SymbolNative,
		)
		if err != nil {
			return fmt.Errorf("insert currency %s: %w", name, err)
		}
	}
	return nil
}
