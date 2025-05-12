# Currency API (Go, Swagger, PosgtgreSQL, Kafka, gRPC, REST)
Server and client application. Server retrieves currency data from https://api.freecurrencyapi.com/v1/currencies then servers them through it's own API at http://localhost:8080 To access server API, u need to request and API key from server http://localhost:8080/api_key

Allows adding new currencies (see swagger documentation). When new currency is added server sends kafka message with date/time and currency code. When server receives gRPC request with currency code it serves full info of that currency to client.

Client listens to kafka messages at the same topic, when receives a new message with code, it concurrently (through worker pool) requests that currency info through gRPC and prints that currency (or error)
## Install
```bash
go build ./server
```
```bash
go build ./client
```
create a ./server/config.json file:
```json
{
    "api_url": "https://api.freecurrencyapi.com/v1/currencies",
    "api_key": "<freecurrencyapi_api_key>",
    "db_conn": "postgres://<user>:<password>@localhost:5432/currencies?sslmode=disable",
    "server_addr": "localhost:50051",
    "log_file_path": "server.log",
    "kafka_addr": "localhost:9092",
    "kafka_topic": "new_currencies"
}
```
create a ./client/config.json file:
```json
{
    "server_addr": "localhost:50051",
    "kafka_addr": "localhost:9092",
    "kafka_topic": "new_currencies"
}
```
launch kafka server at specified address with specified topic name, then launch server and client
```bash
./server
```
```bash
./client
```
## API Documentation
API documentation is available at http://localhost:8080/swagger
Swagger provides UI for testing API and detailed information about requests, responses, status codes, etc.