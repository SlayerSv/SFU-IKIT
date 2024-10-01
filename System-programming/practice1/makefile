CC = gcc

run: build
	./parent.out

build: child.out parent.out

test: test.out
	./test.out

test.out: test.o process_values.o
	$(CC) -o $@ $^ -lcunit

child.out: child.o process_values.o
	$(CC) -o $@ $^

parent.out: main.o input.o
	$(CC) -o $@ $^

main.o: constants.h

child.o: constants.h

process_values.o: constants.h

test.o: constants.h

input.o: constants.h