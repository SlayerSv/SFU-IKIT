#ifndef READER_H
#define READER_H

void reader_read(struct buffer* buffer, char* reader_id);
void* reader_run(void* arg);
extern unsigned int reader_read_time;

#endif