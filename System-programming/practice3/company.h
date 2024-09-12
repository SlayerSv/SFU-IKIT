#ifndef COMPANY
#define COMPANY

const int MAX_NAME_LENGTH = 30;
const int MAX_CITY_LENGTH = 30;
const int VALID = 1;
const int INVALID = 0;

int COMPANY_NEXT_ID = 0;

struct Company {
    int id;
    char name[MAX_NAME_LENGTH + 1];
    char city[MAX_CITY_LENGTH + 1];
    int employees;
};

void company_print(struct Company* c);
int company_is_valid(char* name, char* city, int employees);
void company_delete(struct Company* c);
void company_update(struct Company* c, char* name, char* city, int employees);
struct Company* company_new(char* name, char* city, int employees);

#endif
