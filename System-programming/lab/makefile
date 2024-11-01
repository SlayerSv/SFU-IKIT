CC = gcc

build: server.out client.out

server.out: server.o process_values.o
	$(CC) -o $@ $^

client.out: client.o
	$(CC) -o $@ $^
