#ifndef COMPANY
#define COMPANY

#include "input.h"

struct Company {
    int id;
    char name[MAX_STRING_SIZE + 1];
    char city[MAX_STRING_SIZE + 1];
    int employees;
};

void company_print(struct Company* c);
void company_delete(struct Company* c);
void company_update(struct Company* c, char* name, char* city, int employees);
struct Company* company_new(char* name, char* city, int employees);

#endif
