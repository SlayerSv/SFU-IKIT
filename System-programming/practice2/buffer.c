#include "buffer.h"

/// @brief Add record at the end of a buffer.
/// @param rb Buffer of records to which record will be added.
/// @param new_record Record to be added to the buffer.
void record_buffer_push(struct Record_buffer* rb, struct Record* new_record) {
    struct Record* curr = rb->tail->prev;
    curr->next = new_record;
    new_record->prev = curr;
    new_record->next = rb->tail;
    rb->tail->prev = new_record;
    rb->size++;
}

/// @brief Remove record from the start of a buffer.
/// @param rb Buffer of records from which a record will be removed.
/// @returns Removed record.
struct Record* record_buffer_pop(struct Record_buffer* rb) {
    if (rb->size == 0) {
        return NULL;
    }
    struct Record* curr = rb->head->next;
    rb->head->next = curr->next;
    curr->next->prev = rb->head;
    rb->size--;
    return curr;
}

/// @brief Reads a records from a buffer and then delets it.
/// @param rb Buffer of record from which a record will be read.
/// @param reader Reader that perform the reading operation.
void record_buffer_read(struct Record_buffer* rb, struct Reader* reader) {
    /// wait until there records in the buffer to read.
    sem_wait(&rb->sem_size);
    /// lock the buffer.
    pthread_mutex_lock(&rb->mutex);
    /// reading operation must perform for a certain duration.
    sleep(reader->read_time);
    /// remove a record to read.
    struct Record* record = record_buffer_pop(rb);
    printf("Reader %d reads record %d %s\n", reader->id, record->id, record->text);
    record_delete(record);
    /// signal for writers that buffer is no longer full.
    pthread_cond_signal(&rb->has_space);
    pthread_mutex_unlock(&rb->mutex);
}

/// @brief Create a new record and add it to a buffer.
/// @param rb Buffer of records to which new record will be written.
/// @param writer Writer that performs the writing operation.
void record_buffer_write(struct Record_buffer* rb, struct Writer* writer) {
    pthread_mutex_lock(&rb->mutex);
    /// if buffer is full then wait until readers free some space.
    while (rb->size >= rb->max_size) {
        pthread_cond_wait(&rb->has_space, &rb->mutex);
    }
    /// writing operation must perform for a certain duration.
    sleep(writer->write_time);
    /// create and add a new record.
    char record_text[DEFAULT_BUFFER_SIZE];
    sprintf(record_text, "written by writer %d", writer->id);
    struct Record* record = record_new(record_text);
    record_buffer_push(rb, record);
    printf("Writer %d writes record %d\n", writer->id, record->id);
    /// increase semaphor counter so readers know there are records to read.
    sem_post(&rb->sem_size);
    pthread_mutex_unlock(&rb->mutex);
}   

/// @brief Create, init and return a new buffer of records.
/// @param max_size Max size of the buffer. Writes cannot add more records
/// than this number.
/// @return New buffer ready to use.
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