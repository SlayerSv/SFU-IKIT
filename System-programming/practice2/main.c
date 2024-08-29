# include <pthread.h>
# include <semaphore.h>
# include <string.h>
# include <stdio.h>
# include <stdlib.h>
# include "buffer.h"
# include "reader.h"
# include "writer.h"

int number_of_readers = 1;
int number_of_writers = 1;
int read_time = 1;
int write_time = 1;


int main() {
    pthread_t reader;
    pthread_t writer;
    struct buffer* buffer = buffer_init();
    pthread_create(writer, NULL, run_writer, buffer);
    pthread_create(reader, NULL, run_reader, buffer);
    pthread_join(writer, NULL);
    pthread_join(reader, NULL);
}