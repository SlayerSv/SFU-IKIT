# include <pthread.h>
# include <stdio.h>

# include "buffer.h"

unsigned long long next_reader_id = 0;

void read(struct buffer* buffer, char* reader_id) {
    sem_wait(buffer->records_count);
    pthread_mutex_lock(&buffer->mutex);
    struct record* record = buffer_pop();
    printf("Reader №%s reading: %s\n", reader_id, record->text);
    free(record);
    pthread_cond_signal(buffer->buffer_full);
    pthread_mutex_unlock(&buffer->mutex);
}

void* run_reader(void* arg) {
    struct buffer* buffer = (struct buffer*) arg;
    char reader_id[20];
    sprintf(reader_id, ++next_reader_id);
    while (1) {
        read(buffer, reader_id);
    }
}
