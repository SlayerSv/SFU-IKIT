#include "buffer.h"

int max_buffer_size = 0;

void buffer_push(struct buffer* buffer, struct record* new_record) {
    struct record* curr = buffer->tail->prev;
    curr->next = new_record;
    new_record->prev = curr;
    new_record->next = buffer->tail;
    buffer->tail->prev = new_record;
}

struct record* buffer_pop(struct buffer* buffer) {
    struct record* curr = buffer->head->next;
    buffer->head->next = curr->next;
    curr->next->prev = buffer->head;
    return curr;
}

void buffer_next_message(struct buffer* buffer, char* message) {
    struct record* record = buffer_pop(buffer);
    strcpy(message, record->text);
    free(record);
}

void buffer_new_message(struct buffer* buffer, char* message) {
    struct record* new_record = (struct record*) malloc(sizeof(struct record));
    strcpy(new_record->text, message);
    buffer_push(buffer, new_record);
}   

struct buffer* buffer_new() {
    struct buffer* buffer = (struct buffer*) malloc(sizeof(struct buffer));
    pthread_mutex_init(&buffer->mutex, NULL);
    pthread_mutex_init(&buffer->write_mutex, NULL);
    pthread_mutex_init(&buffer->cond_mutex, NULL);
    sem_init(&buffer->records_count, 0, 0);
    pthread_cond_init(&buffer->buffer_full, NULL);
    buffer->next_id = 0;
    buffer->head = (struct record*) malloc(sizeof(struct record));
    buffer->tail = (struct record*) malloc(sizeof(struct record));
    buffer->head->next = buffer->tail;
    buffer->tail->prev = buffer->head;
    return buffer;
}