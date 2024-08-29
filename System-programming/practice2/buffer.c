#include <pthread.h>
#include <semaphore.h>

int max_buffer_size = 1;

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

void buffer_push(struct buffer* buffer, char* message) {
    struct record* curr = buffer->tail->prev;
    struct record* new_record = (struct record*) malloc(sizeof(struct record));
    strcopy(new_record->text, message);
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