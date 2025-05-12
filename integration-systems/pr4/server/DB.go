package main

import (
	"database/sql"
	"errors"
	"fmt"
	"os"
	"strings"
	"time"

	"github.com/lib/pq"
)

type PostgresDB struct {
	*sql.DB
	lastUpdate time.Time
}

func NewPostgres() (*PostgresDB, error) {
	host := os.Getenv("POSTGRES_HOST")
	port := os.Getenv("POSTGRES_PORT")
	user := os.Getenv("POSTGRES_USER")
	pass := os.Getenv("POSTGRES_PASSWORD")
	dbname := os.Getenv("POSTGRES_DB")
	conn := fmt.Sprintf("host=%s port=%s user=%s password=%s dbname=%s sslmode=disable",
		host, port, user, pass, dbname)
	db, err := sql.Open("postgres", conn)
	if err != nil {
		return nil, err
	}
	fmt.Println(conn)
	p := &PostgresDB{db, time.Time{}}
	return p, nil
}

func (db *PostgresDB) InsertCurrencies(currencies map[string]Currency) error {
	insertSQL := `
		INSERT INTO currencies
		VALUES ($1, $2, $3, $4, $5)
	`
	for name, currency := range currencies {
		_, err := db.Exec(insertSQL,
			strings.ToUpper(currency.Code),
			currency.Name,
			currency.NamePlural,
			currency.Symbol,
			currency.SymbolNative,
		)
		if err != nil {
			return fmt.Errorf("insert currency %s: %w", name, err)
		}
	}
	db.lastUpdate = time.Now()
	return nil
}

func (db *PostgresDB) GetCurrencies() ([]Currency, error) {
	rows, err := db.Query("SELECT * FROM currencies order by name")
	if err != nil {
		return nil, err
	}
	return db.extractCurrencies(rows)
}

func (db *PostgresDB) InsertCurrency(c Currency) (Currency, error) {
	row := db.QueryRow("Insert into currencies values($1, $2, $3, $4, $5) returning *",
		strings.ToUpper(c.Code), c.Name, c.NamePlural, c.Symbol, c.SymbolNative)
	c, err := db.extractCurrency(row)
	var pgErr *pq.Error
	if errors.As(err, &pgErr) {
		switch pgErr.Code {
		case "23505":
			return c, errAlreadyExists
		}
	}
	if err == nil {
		db.lastUpdate = time.Now()
	}
	return c, err
}

func (db *PostgresDB) GetCurrencyCount() (int, error) {
	row := db.QueryRow("SELECT count(*) FROM currencies")
	var count int
	err := row.Scan(&count)
	return count, err
}

func (db *PostgresDB) GetCurrencyByCode(code string) (Currency, error) {
	row := db.QueryRow("SELECT * FROM currencies where code = $1", strings.ToUpper(code))
	c, err := db.extractCurrency(row)
	if err == sql.ErrNoRows {
		return c, errNotFound
	}
	return c, err
}

func (db *PostgresDB) extractCurrency(row *sql.Row) (Currency, error) {
	currency := Currency{}
	err := row.Scan(&currency.Code, &currency.Name, &currency.NamePlural,
		&currency.Symbol, &currency.SymbolNative, &currency.CreatedAt, &currency.UpdatedAt)
	return currency, err
}

func (db *PostgresDB) extractCurrencies(rows *sql.Rows) ([]Currency, error) {
	defer rows.Close()
	currencies := []Currency{}
	for rows.Next() {
		currency := Currency{}
		err := rows.Scan(&currency.Code, &currency.Name, &currency.NamePlural,
			&currency.Symbol, &currency.SymbolNative, &currency.CreatedAt, &currency.UpdatedAt)
		if err != nil {
			return nil, err
		}
		currencies = append(currencies, currency)
	}
	return currencies, nil
}

func (db *PostgresDB) HasAPIKey(APIKey string) (bool, error) {
	row := db.QueryRow("SELECT * FROM api_keys where key = $1", APIKey)
	var k string
	err := row.Scan(&k)
	if err == sql.ErrNoRows {
		return false, nil
	}
	if err == nil {
		return true, nil
	}
	return false, err
}

func (db *PostgresDB) AddAPIKey(APIKey string) error {
	_, err := db.Exec("Insert into api_keys values($1)", APIKey)
	return err
}

func (db *PostgresDB) UpdatedAt() (time.Time, error) {
	row := db.QueryRow("SELECT updated_at FROM currencies order by updated_at desc limit 1")
	var updatedAt time.Time
	err := row.Scan(&updatedAt)
	return updatedAt, err
}
