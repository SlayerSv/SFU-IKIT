#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#include "company.h"

int COMPANY_NEXT_ID = 0;

struct Company* company_new(char* name, char* city, int employees) {
    struct Company* c = malloc(sizeof(struct Company));
    strcpy(c->name, name);
    strcpy(c->city, city);
    c->employees = employees;
    c->id = ++COMPANY_NEXT_ID;
    return c;
}

void company_update(struct Company* c, char* name, char* city, int employees) {
    strcpy(c->name, name);
    strcpy(c->city, city);
    c->employees = employees;
}

void company_delete(struct Company* c) {
    free(c);
}

void company_print(struct Company* c) {
    printf("\nID: %d\nName: %s\nCity: %s\nEmployees: %d\n", c->id, c->name, c->city, c->employees);
}
