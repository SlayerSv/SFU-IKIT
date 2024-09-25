#include "buffer.h"

void record_buffer_push(struct Record_buffer* rb, struct Record* new_record) {
    struct Record* curr = rb->tail->prev;
    curr->next = new_record;
    new_record->prev = curr;
    new_record->next = rb->tail;
    rb->tail->prev = new_record;
    rb->size++;
}

struct Record* record_buffer_pop(struct Record_buffer* rb) {
    struct Record* curr = rb->head->next;
    rb->head->next = curr->next;
    curr->next->prev = rb->head;
    rb->size--;
    return curr;
}

void record_buffer_read(struct Record_buffer* rb, struct Reader* reader) {
    sem_wait(&rb->sem_size);
    pthread_mutex_lock(&rb->mutex);
    sleep(reader->read_time);
    struct Record* record = record_buffer_pop(rb);
    printf("%d Reader %d reads record %d %s\n", rb->size, reader->id, record->id, record->text);
    record_delete(record);
    pthread_cond_signal(&rb->has_space);
    pthread_mutex_unlock(&rb->mutex);
}

void record_buffer_write(struct Record_buffer* rb, struct Writer* writer) {
    pthread_mutex_lock(&rb->mutex);
    while (rb->size >= rb->max_size) {
        pthread_cond_wait(&rb->has_space, &rb->mutex);
    }
    sleep(writer->write_time);
    char record_text[DEFAULT_BUFFER_SIZE];
    sprintf(record_text, "written by writer %d", writer->id);
    struct Record* record = record_new(record_text);
    record_buffer_push(rb, record);
    printf("%d Writer %d writes record %d\n", rb->size, writer->id, record->id);
    sem_post(&rb->sem_size);
    pthread_mutex_unlock(&rb->mutex);
}   

struct Record_buffer* record_buffer_new(int max_size) {
    struct Record_buffer* rb = malloc(sizeof(struct Record_buffer));

    pthread_mutex_init(&rb->mutex, NULL);
    sem_init(&rb->sem_size, 0, 0);
    pthread_cond_init(&rb->has_space, NULL);

    rb->max_size = max_size;
    rb->next_record_id = 0;
    rb->size = 0;

    rb->head = malloc(sizeof(struct Record));
    rb->tail = malloc(sizeof(struct Record));
    rb->head->next = rb->tail;
    rb->tail->prev = rb->head;

    return rb;
}