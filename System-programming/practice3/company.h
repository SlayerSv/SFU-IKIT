#ifndef COMPANY
#define COMPANY

#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#include "constants.h"

#define MAX_COMPANY_NAME_SIZE DEFAULT_BUFFER_SIZE - 1
#define MAX_COMPANY_CITY_SIZE DEFAULT_BUFFER_SIZE - 1

/// Struct representing a company.
struct Company {
    int id;
    /// Name of the company.
    char* name;
    /// Location (city) of the company.
    char* city;
    /// Number of employees.
    int employees;
};

void company_print(struct Company* company);
void company_delete(struct Company* company);
void company_update(struct Company* company, char* name, char* city, int employees);
struct Company* company_new(char* name, char* city, int employees);

#endif
