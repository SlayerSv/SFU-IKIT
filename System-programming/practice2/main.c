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
    printf("%s\n", "HELLO");
    srand(time(NULL));
    for (int i = 0; i < number_of_readers; i++) {
        pthread_t reader;
        pthread_create(&reader, NULL, reader_run, buffer);
        //pthread_join(reader, NULL);
    }
    for (int i = 0; i < number_of_writers; i++) {
        pthread_t writer;
        pthread_create(&writer, NULL, writer_run, buffer);
        //pthread_join(writer, NULL);
    }
    sleep(20);
}