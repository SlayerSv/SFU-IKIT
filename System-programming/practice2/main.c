#include <time.h>

#include "reader.h"
#include "writer.h"
#include "buffer.h"
#include "input.h"

int main() {
    int max_record_buffer_size;
    input_take_uint(&max_record_buffer_size, "Enter max record buffer size: ");

    int number_of_readers = 0;
    input_take_uint(&number_of_readers, "Enter number of readers: ");

    int number_of_writers = 0;
    input_take_uint(&number_of_writers, "Enter number of writers: ");

    int read_time;
    input_take_uint(&read_time, "Enter read time: ");

    int write_time;
    input_take_uint(&write_time, "Enter write time: ");
    
    struct Record_buffer* rb = record_buffer_new(max_record_buffer_size);
    srand(time(NULL));

    /// create and run entered number of readers as concurrent threads.
    pthread_t readers[number_of_readers];
    struct Reader_arg reader_args[number_of_readers];
    for (int i = 0; i < number_of_readers; i++) {
        reader_args[i].reader = reader_new(read_time);
        reader_args[i].rb = rb;
        pthread_create(&readers[i], NULL, reader_run, &reader_args[i]);
    }

    /// create and run entered number of writers as concurrent threads.
    pthread_t writers[number_of_writers];
    struct Writer_arg writer_args[number_of_writers];
    for (int i = 0; i < number_of_writers; i++) {
        writer_args[i].writer = writer_new(write_time);
        writer_args[i].rb = rb;
        pthread_create(&writers[i], NULL, writer_run, &writer_args[i]);
    }

    /// wait for threads to finish.
    for (int i = 0; i < number_of_readers; i++) {
        pthread_join(readers[i], NULL);
    }
    for (int i = 0; i < number_of_writers; i++) {
        pthread_join(writers[i], NULL);
    }
}