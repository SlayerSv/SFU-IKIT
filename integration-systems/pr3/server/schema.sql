DROP TABLE IF EXISTS currencies;
CREATE TABLE currencies (
    code varchar(3) PRIMARY KEY,
    name text NOT NULL UNIQUE,
    name_plural text NOT NULL UNIQUE,
    symbol text NOT NULL UNIQUE,
    symbol_native text NOT NULL,
    created_at TIMESTAMP default CURRENT_TIMESTAMP,
    updated_at TIMESTAMP default CURRENT_TIMESTAMP
);
create index on currencies(updated_at);
DROP TABLE IF EXISTS api_keys;
CREATE TABLE api_keys (
    key text PRIMARY KEY
);