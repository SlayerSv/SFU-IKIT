CC = gcc

run: build
	./practice4.out

build: practice4.out

practice4.out : main.o input.o companyfile.o
	$(CC) -o $@ $^

main.o : constants.h

input.o: constants.h

companyfile.o: constants.h