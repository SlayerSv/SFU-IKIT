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

struct Record_buffer {
    int max_size;
    int size;
    pthread_mutex_t mutex;
    sem_t sem_size;
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