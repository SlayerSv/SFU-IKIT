#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#include "buffer.h"
#include "reader.h"

unsigned long long next_reader_id = 0;
unsigned int reader_read_time = 1;

void reader_read(struct buffer* buffer, char* reader_id) {
    sem_wait(&buffer->records_count);
    pthread_mutex_lock(&buffer->mutex);
    char message[100];
    buffer_next_message(buffer, message);
    sleep(reader_read_time);
    printf("Reader №%s reading: %s\n", reader_id, message);
    pthread_cond_signal(&buffer->buffer_full);
    pthread_mutex_unlock(&buffer->mutex);
    sleep(rand() % 10);
}

void* reader_run(void* arg) {
    struct buffer* buffer = (struct buffer*) arg;
    char reader_id[20];
    sprintf(reader_id, "%llu", ++next_reader_id);
    while (1) {
        reader_read(buffer, reader_id);
    }
}
