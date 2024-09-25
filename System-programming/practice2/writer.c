#include "writer.h"
#include "buffer.h"

int next_writer_id = 0;

struct Writer* writer_new(int write_time) {
    struct Writer* writer = malloc(sizeof(struct Writer));
    writer->id = ++next_writer_id;
    writer->write_time = write_time;
    return writer;
}

void* writer_run(void* arg) {
    struct Writer_arg* warg = (struct Writer_arg*) arg;
    while (1) {
        record_buffer_write(warg->rb, warg->writer);
        sleep(rand() % MAX_SLEEP_TIME);
    }
}