#include <pthread.h>
#include <semaphore.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <unistd.h>
//#include <windows.h>
#include "buffer.h"
#include "reader.h"
#include "writer.h"

int main() {
    printf("Enter max buffer size: ");
    scanf("%u", &max_buffer_size);

    unsigned int number_of_readers = 1;
    printf("Enter number of readers: ");
    scanf("%u", &number_of_readers);

    unsigned int number_of_writers = 1;
    printf("Enter number of writers: ");
    scanf("%u", &number_of_writers);

    printf("Enter read time: ");
    scanf("%u", &reader_read_time);

    printf("Enter write time: ");
    scanf("%u", &writer_write_time);
    
    struct buffer* buffer = buffer_init();
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