#ifndef WRITER_H
#define WRITER_H

#include <pthread.h>
#include <semaphore.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>

void writer_write(struct buffer* buffer, char* writer_id);
void* writer_run(void* arg);

#endif