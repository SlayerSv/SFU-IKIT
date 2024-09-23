#include "buffer.h"
#include "writer.h"
#include "constants.h"

unsigned long long next_writer_id = 0;
int writer_write_time = 0;

void writer_write(struct buffer* buffer, char* writer_id) {
    pthread_mutex_lock(&buffer->write_mutex);
    int records_count;
    sem_getvalue(&buffer->records_count, &records_count);
    extern int max_buffer_size;
    while (records_count >= max_buffer_size) {
        pthread_cond_wait(&buffer->buffer_full, &buffer->cond_mutex);
        sem_getvalue(&buffer->records_count, &records_count);
    }
    pthread_mutex_lock(&buffer->mutex);
    char message[DEFAULT_BUFF_SIZE];
    sprintf(message, "Message %llu", ++buffer->next_id);
    buffer_new_message(buffer, message);
    printf("Writer %s writing: %s\n", writer_id, message);
    sleep(writer_write_time);
    sem_post(&buffer->records_count);
    pthread_mutex_unlock(&buffer->mutex);
    pthread_mutex_unlock(&buffer->write_mutex);
    sleep(rand() % MAX_SLEEP_TIME);
}

void* writer_run(void* arg) {
    struct buffer* buffer = (struct buffer*) arg;
    char writer_id[DEFAULT_BUFF_SIZE];
    sprintf(writer_id, "%llu", ++next_writer_id);
    while (1) {
        writer_write(buffer, writer_id);
    }
}