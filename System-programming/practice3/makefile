CC = gcc

run: practice3.out
	./practice3.out

build: practice3.out

practice3.out: main.o input.o company.o companylist.o controller.o
	$(CC) -o $@ $^

controller.o: constants.h

companylist.o: constants.h

company.o: constants.h

main.o: constants.h

input.o: constants.h