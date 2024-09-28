#include "company.h"

int company_next_id = 0;

/// @brief Creates and returns new company struct with provided fields.
/// @param name Name of the company.
/// @param city Location (city) of the company.
/// @param employees Number of employees of the company.
/// @return Created company struct.
struct Company* company_new(char* name, char* city, int employees) {
    struct Company* company = malloc(sizeof(struct Company));
    company->name = malloc((MAX_COMPANY_NAME_SIZE + 1) * sizeof(char));
    company->city = malloc((MAX_COMPANY_CITY_SIZE + 1) * sizeof(char));
    company_update(company, name, city, employees);
    company->id = ++company_next_id;
    return company;
}

/// @brief Updates company replacing all fields with provided arguments.
/// @param company Company to update.
/// @param name New name of the company.
/// @param city New location (city) of the company.
/// @param employees New number of employees of the company.
void company_update(struct Company* company, char* name, char* city, int employees) {
    company->name[0] = '\0';
    company->name[MAX_COMPANY_NAME_SIZE] = '\0';
    strncat(company->name, name, MAX_COMPANY_NAME_SIZE);
    company->city[0] = '\0';
    company->city[MAX_COMPANY_CITY_SIZE] = '\0';
    strncat(company->city, city, MAX_COMPANY_CITY_SIZE);
    company->employees = employees;
}

/// @brief Deletes a company, frees memory.
/// @param company Company to delete.
void company_delete(struct Company* company) {
    free(company->name);
    free(company->city);
    free(company);
}

/// @brief Prints information about company.
/// @param company Company to print. 
void company_print(struct Company* company) {
    printf("\nID: %d\nName: %s\nCity: %s\nEmployees: %d\n",
        company->id, company->name, company->city, company->employees);
}
