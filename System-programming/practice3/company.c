#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#include "company.h"

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

int company_is_valid(char* name, char* city, int employees) {
    if (strlen(name) > MAX_NAME_LENGTH) {
        printf("Company name cannot exceed %d symbols", MAX_NAME_LENGTH);
        return INVALID;
    }
    if (strlen(city) > MAX_CITY_LENGTH) {
        printf("City name cannot exceed %d symbols", MAX_CITY_LENGTH);
        return INVALID;
    }
    if (employees < 0) {
        printf("Company number of employees cannot be negative");
        return INVALID;
    }
    return VALID;
}

void company_delete(struct Company* c) {
    free(c);
}

void company_print(struct Company* c) {
    printf("ID: %d\nName: %s\nCity: %s\nEmployees: %d", c->id, c->name, c->city, c->employees);
}
