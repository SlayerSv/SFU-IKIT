CC = gcc

run: practice2.out
	./practice2.out

build: practice2.out

practice2.out: main.o input.o buffer.o reader.o writer.o record.o
	$(CC) -o $@ $^

reader.o: constants.h

writer.o: constants.h

input.o: constants.h

record.o: constants.h

buffer.o: constants.h