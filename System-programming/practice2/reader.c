#include "buffer.h"
#include "reader.h"
#include "constants.h"

unsigned long long next_reader_id = 0;
int reader_read_time = 0;

void reader_read(struct buffer* buffer, char* reader_id) {
    sem_wait(&buffer->records_count);
    pthread_mutex_lock(&buffer->mutex);
    char message[DEFAULT_BUFF_SIZE];
    buffer_next_message(buffer, message);
    printf("Reader %s reading: %s\n", reader_id, message);
    sleep(reader_read_time);
    pthread_cond_signal(&buffer->buffer_full);
    pthread_mutex_unlock(&buffer->mutex);
    sleep(rand() % MAX_SLEEP_TIME);
}

void* reader_run(void* arg) {
    struct buffer* buffer = (struct buffer*) arg;
    char reader_id[DEFAULT_BUFF_SIZE];
    sprintf(reader_id, "%llu", ++next_reader_id);
    while (1) {
        reader_read(buffer, reader_id);
    }
}
