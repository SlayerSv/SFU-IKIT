#ifndef READER_H
#define READER_H

#include <unistd.h>
#include "constants.h"

struct Reader {
    int id;
    int read_time;
};

struct Reader_arg {
    struct Reader* reader;
    struct Record_buffer* rb;
};

void* reader_run(void* arg);
struct Reader* reader_new(int read_time);

#endif