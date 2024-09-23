#include <time.h>

#include "buffer.h"
#include "reader.h"
#include "writer.h"
#include "input.h"

int main() {
    extern int max_buffer_size;
    input_take_uint(&max_buffer_size, "Enter max buffer size: ");

    int number_of_readers = 0;
    input_take_uint(&number_of_readers, "Enter number of readers: ");

    int number_of_writers = 0;
    input_take_uint(&number_of_writers, "Enter number of writers: ");

    extern int reader_read_time;
    input_take_uint(&reader_read_time, "Enter read time: ");

    extern int writer_write_time;
    input_take_uint(&writer_write_time, "Enter write time: ");
    
    struct buffer* buffer = buffer_new();
    srand(time(NULL));
    pthread_t readers[number_of_readers];
    for (int i = 0; i < number_of_readers; i++) {
        pthread_create(&readers[i], NULL, reader_run, buffer);
    }
    pthread_t writers[number_of_writers];
    for (int i = 0; i < number_of_writers; i++) {
        pthread_create(&writers[i], NULL, writer_run, buffer);
    }
    for (int i = 0; i < number_of_readers; i++) {
        pthread_join(readers[i], NULL);
    }
    for (int i = 0; i < number_of_writers; i++) {
        pthread_join(writers[i], NULL);
    }
}