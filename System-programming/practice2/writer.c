#include <pthread.h>
#include <semaphore.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include "buffer.h"
#include "writer.h"

unsigned long long next_writer_id = 0;

void writer_write(struct buffer* buffer, char* writer_id) {
    pthread_mutex_lock(&buffer->write_mutex);
    int records_count;
    sem_getvalue(&buffer->records_count, &records_count);
    printf("Sem: %d Writer: %s\n", records_count, writer_id);
    while (records_count >= max_buffer_size) {
        pthread_cond_wait(&buffer->buffer_full, &buffer->cond_mutex);
        sem_getvalue(&buffer->records_count, &records_count);
    }
    pthread_mutex_lock(&buffer->mutex);
    sleep(writer_write_time);
    char message[50];
    sprintf(message, "Message %llu", ++buffer->next_id);
    buffer_new_message(buffer, message);
    printf("Writer %s writing: %s\n", writer_id, message);
    sem_post(&buffer->records_count);
    pthread_mutex_unlock(&buffer->mutex);
    pthread_mutex_unlock(&buffer->write_mutex);
    sleep(rand() % 10);
}

void* writer_run(void* arg) {
    struct buffer* buffer = (struct buffer*) arg;
    char writer_id[20];
    sprintf(writer_id, "%llu", ++next_writer_id);
    while (1) {
        writer_write(buffer, writer_id);
    }
}