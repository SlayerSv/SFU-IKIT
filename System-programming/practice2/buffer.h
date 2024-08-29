# include <pthread.h>
# include <semaphore.h>
int max_buffer_size;

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

struct record* buffer_pop();
void buffer_push(struct buffer*, char*);
struct buffer* buffer_init();