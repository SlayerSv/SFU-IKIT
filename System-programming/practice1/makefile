CC = gcc

build: child.out parent.out

run:
	./parent.out

child.out: process_values.o
	$(CC) -o $@ $^

parent.out: main.o
	$(CC) -o $@ $^