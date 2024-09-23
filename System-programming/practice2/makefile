CC = gcc

run: practice2.out
	./practice2.out

build: practice2.out

practice2.out: main.o input.o buffer.o reader.o writer.o
	$(CC) -o $@ $^