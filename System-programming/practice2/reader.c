#include "reader.h"
#include "buffer.h"

int next_reader_id = 0;

struct Reader* reader_new(int read_time) {
    struct Reader* reader = malloc(sizeof(struct Reader));
    reader->id = ++next_reader_id;
    reader->read_time = read_time;
    return reader;
}

void* reader_run(void* arg) {
    struct Reader_arg* rarg = (struct Reader_arg*) arg;
    while (1) {
        record_buffer_read(rarg->rb, rarg->reader);
        int sleep_time = rand() % MAX_SLEEP_TIME;
        sleep(sleep_time);
    }
}
