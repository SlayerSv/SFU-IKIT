#ifndef WRITER_H
#define WRITER_H

void writer_write(struct buffer* buffer, char* writer_id);
void* writer_run(void* arg);
unsigned int writer_write_time;

#endif