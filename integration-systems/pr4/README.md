# Currency API (Go, Swagger, PosgtgreSQL, Kafka, gRPC, REST)
Server and client application. Server retrieves currency data from https://api.freecurrencyapi.com/v1/currencies then servers them through it's own API at http://localhost:8080 To access server API, u need to request and API key from server http://localhost:8080/api_key

Allows adding new currencies (see swagger documentation). When new currency is added server sends kafka message to topic new_currencies with date/time and currency code. When server receives gRPC request with currency code it serves full info of that currency to client.

Client listens to kafka messages at the same topic, when receives a new message with code, it concurrently (through worker pool) requests that currency info through gRPC and prints that currency (or error)
## Install
```bash
go build ./server
```
```bash
go build ./client
```
launch kafka server at localhost:9092 with topic new_currencies, then launch server and client
```bash
./server
```
```bash
./client
```
## API Documentation
API documentation is available at http://localhost:8080/swagger
Swagger provides UI for testing API and detailed information about requests, responses, status codes, etc.