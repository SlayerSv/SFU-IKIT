#ifndef BUFFER_H
#define BUFFER_H

#include <pthread.h>
#include <semaphore.h>
#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include "constants.h"
#include "record.h"
#include "reader.h"
#include "writer.h"

/// @brief Double linked list of records for async access by many threads.
/// Implemented as a queue (first in - first out).
struct Record_buffer {
    /// cannot write more records than this value.
    int max_size;
    /// current size of the buffer.
    int size;
    /// mutex that allows only one thread to read/write from/to buffer.
    pthread_mutex_t mutex;
    /// semaphor for records count in the buffer.
    sem_t sem_size;
    /// cond variable if the buffer is full or not.
    pthread_cond_t has_space;

    int next_record_id;
    struct Record* head;
    struct Record* tail;
};

struct Record* record_buffer_pop(struct Record_buffer* rb);
void record_buffer_push(struct Record_buffer* rb, struct Record* new_record);
struct Record_buffer* record_buffer_new(int max_size);
void record_buffer_read(struct Record_buffer* rb, struct Reader* reader);
void record_buffer_write(struct Record_buffer* rb, struct Writer* writer);

#endif