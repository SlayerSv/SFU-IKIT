# include <pthread.h>
# include <semaphore.h>
# include <string.h>
#include <stdio.h>
#include <stdlib.h>

pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
int max_buffer_size = 1;
int number_of_readers = 1;
int number_of_writers = 1;
int read_time = 1;
int write_time = 1;



struct record {
    char text[40];
    struct record* prev;
    struct record* next;
};

struct buffer {
    pthread_mutex_t mutex;
    sem_t records_count;
    pthread_cond_t buffer_full;
    pthread_mutex_t write_mutex;
    unsigned long long next_id;
    struct record* head;
    struct record* tail;
};

struct buffer* buffer_init() {
    struct buffer* buffer = (struct buffer*) malloc(sizeof(struct buffer));
    pthread_mutex_init(buffer->mutex, NULL);
    pthread_mutex_init(buffer->write_mutex, NULL);
    sem_init(buffer->records_count, 0, 0);
    pthread_cond_init(buffer->buffer_full, NULL);
    buffer->next_id = 0;
    buffer->head = (struct record*) malloc(sizeof(struct record));
    buffer->tail = (struct record*) malloc(sizeof(struct record));
    buffer->head->next = buffer->tail;
    buffer->tail->prev = buffer->head;
}

void read(struct buffer* buff) {
    sem_wait(buff->records_count);
    pthread_mutex_lock(&buff->mutex);
    struct record* curr = buff->head->next;
    printf("Reading message: %s\n", curr->text);
    buff->head->next = curr->next;
    curr->next->prev = buff->head;
    free(curr);
    pthread_cond_signal(buff->buffer_full);
    pthread_mutex_unlock(&buff->mutex);
}

void write(struct buffer* buff) {
    pthread_mutex_lock(buff->write_mutex);
    int records_count;
    sem_getvalue(buff->records_count, records_count);
    while (records_count >= max_buffer_size) {
        pthread_cond_wait(buff->buffer_full, buff->write_mutex);
        sem_getvalue(buff->records_count, records_count);
    }
    pthread_mutex_lock(buff->mutex);
    struct record* curr = buff->tail->prev;
    struct record* new_record = (struct record*) malloc(sizeof(struct record));
    char number[20];
    sprintf(number, "%llu", ++buff->next_id);
    strcopy(new_record->text, "Record № ");
    strcat(new_record->text, number);
    printf("Writing message: ", new_record->text);
    curr->next = new_record;
    new_record->prev = curr;
    new_record->next = buff->tail;
    buff->tail->prev = new_record;
    sem_post(buff->records_count);
    pthread_mutex_unlock(buff->mutex);
    pthread_mutex_unlock(buff->write_mutex);
}

void* run_writer(void* arg) {
    struct buffer* buffer = (struct buffer*) arg;
    while (1) {
        write(buffer);
    }
}

void* run_reader(void* arg) {
    struct buffer* buffer = (struct buffer*) arg;
    while (1) {
        read(buffer);
    }
}

int main() {
    pthread_t reader;
    pthread_t writer;
    struct buffer* buffer = buffer_init();
    pthread_create(writer, NULL, run_writer, buffer);
    pthread_create(reader, NULL, run_reader, buffer);
    pthread_join(writer, NULL);
    pthread_join(reader, NULL);
}