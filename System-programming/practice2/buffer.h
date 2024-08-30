# include <pthread.h>
# include <semaphore.h>
#ifndef BUFFER_H
#define BUFFER_H
extern unsigned int max_buffer_size;

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
    pthread_mutex_t cond_mutex;
    unsigned long long next_id;
    struct record* head;
    struct record* tail;
};

struct record* buffer_pop(struct buffer* buffer);
void buffer_push(struct buffer* buffer, struct record* new_record);
struct buffer* buffer_init();
void buffer_next_message(struct buffer* buffer, char* message);
void buffer_new_message(struct buffer* buffer, char* message);
#endif