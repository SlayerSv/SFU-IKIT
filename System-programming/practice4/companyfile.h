#ifndef COMPANY_FILE
#define COMPANY_FILE
#include "company.h"

void file_add_company(char* name, char* city, char* employees);
void file_write_company(int fd, char* id, char* name, char* city, char* employees);
void file_update_company(int id, char* name, char* city, char* employees);
void file_print_company(int id);
void file_print_all();
void file_delete_company(int id);
#endif