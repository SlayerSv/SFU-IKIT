#ifndef READER_H
#define READER_H

#include <unistd.h>

void reader_read(struct buffer* buffer, char* reader_id);
void* reader_run(void* arg);

#endif