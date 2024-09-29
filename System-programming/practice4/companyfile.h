#ifndef COMPANY_FILE
#define COMPANY_FILE

#include <fcntl.h>
#include <stdio.h>
#include <sys/stat.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

#include "input.h"
#include "constants.h"

void file_add_company(char* name, char* city, char* employees);
void file_write_company(int fd, char* id, char* name, char* city, char* employees);
void file_update_company(int id, char* name, char* city, char* employees);
void file_print_company(int id);
void file_print_all();
void file_delete_company(int id);
void file_print_most_employed();
void file_print_cities_count();

#endif