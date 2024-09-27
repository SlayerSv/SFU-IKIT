#include "company.h"

int company_next_id = 0;

struct Company* company_new(char* name, char* city, int employees) {
    struct Company* company = malloc(sizeof(struct Company));
    company->name = malloc((MAX_COMPANY_NAME_SIZE + 1) * sizeof(char));
    company->city = malloc((MAX_COMPANY_CITY_SIZE + 1) * sizeof(char));
    company_update(company, name, city, employees);
    company->id = ++company_next_id;
    return company;
}

void company_update(struct Company* company, char* name, char* city, int employees) {
    company->name[0] = '\0';
    company->name[MAX_COMPANY_NAME_SIZE] = '\0';
    strncat(company->name, name, MAX_COMPANY_NAME_SIZE);
    company->city[0] = '\0';
    company->city[MAX_COMPANY_CITY_SIZE] = '\0';
    strncat(company->city, city, MAX_COMPANY_CITY_SIZE);
    company->employees = employees;
}

void company_delete(struct Company* company) {
    free(company->name);
    free(company->city);
    free(company);
}

void company_print(struct Company* c) {
    printf("\nID: %d\nName: %s\nCity: %s\nEmployees: %d\n", c->id, c->name, c->city, c->employees);
}
