CC = gcc

build: child.out parent.out

run: child.out parent.out
	./parent.out

test: test.out
	./test.out

test.out: test.o process_values.o
	$(CC) -o $@ $^ -lcunit

child.out: child.o process_values.o
	$(CC) -o $@ $^

parent.out: main.o
	$(CC) -o $@ $^