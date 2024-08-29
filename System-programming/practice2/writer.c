# include <pthread.h>
# include <semaphore.h>
# include "buffer.h"

unsigned long long next_writer_id = 0;

void write(struct buffer* buffer, char* writer_id) {
    pthread_mutex_lock(buffer->write_mutex);
    int records_count;
    sem_getvalue(buffer->records_count, records_count);
    while (records_count >= max_buffer_size) {
        pthread_cond_wait(buffer->buffer_full, buffer->write_mutex);
        sem_getvalue(buffer->records_count, records_count);
    }
    pthread_mutex_lock(buffer->mutex);
    char message[50];
    sprintf(message, "Message №%llu", ++buffer->next_id);
    buffer_push(buffer, message);
    printf("Writer №%s writing: %s", writer_id, message);
    sem_post(buffer->records_count);
    pthread_mutex_unlock(buffer->mutex);
    pthread_mutex_unlock(buffer->write_mutex);
}

void* run_writer(void* arg) {
    struct buffer* buffer = (struct buffer*) arg;
    char writer_id[20];
    sprintf(writer_id, ++next_writer_id);
    while (1) {
        write(buffer, writer_id);
    }
}