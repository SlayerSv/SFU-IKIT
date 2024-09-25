#ifndef RECORD
#define RECORD

#include <string.h>
#include <stdlib.h>
#include "constants.h"

struct Record {
    int id;
    char* text;

    struct Record* prev;
    struct Record* next;
};

struct Record* record_new(char* text);
void record_delete(struct Record* r);

#endif