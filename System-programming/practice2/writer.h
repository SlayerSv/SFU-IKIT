#ifndef WRITER_H
#define WRITER_H

struct Writer {
    int id;
    int write_time;
};

struct Writer_arg {
    struct Writer* writer;
    struct Record_buffer* rb;
};

struct Writer* writer_new(int write_time);
void* writer_run(void* arg);

#endif