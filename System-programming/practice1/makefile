CC = gcc

build: child.out parent.out

run:
	./parent.out

tests: test.o process_values.o
	$(CC) -o $@ $^ -lcunit
	./tests

child.out: child.o process_values.o
	$(CC) -o $@ $^

parent.out: main.o
	$(CC) -o $@ $^