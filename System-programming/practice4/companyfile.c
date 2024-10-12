#include "companyfile.h"

#define FILE_PATH "database"

const int STRING_FIELD_SIZE = MAX_STRING_SIZE + 1;
const int INT_FIELD_SIZE = MAX_INT_SIZE + 1;
const int RECORD_SIZE = STRING_FIELD_SIZE * 2 + INT_FIELD_SIZE * 2;

/// @brief Adds a new company.
/// @param name Name of the company.
/// @param city Location (city) of the company.
/// @param employees Number of employees of the company.
void file_add_company(char* name, char* city, char* employees) {
    int fd = open(FILE_PATH, O_CREAT|O_APPEND|O_WRONLY, 0644);
    struct stat fi;
    fstat(fd, &fi);
    /// calculate next id based on number of records.
    int id = fi.st_size / RECORD_SIZE + 1;
    file_write_company(fd, id, name, city, employees);
    close(fd);
}

/// @brief Updates company info.
/// @param id id of the company.
/// @param name New name of the company.
/// @param city New location (city) of the company.
/// @param employees New number of employees of the company.
void file_update_company(int id, char* name, char* city, char* employees) {
    int fd = open(FILE_PATH, O_CREAT|O_RDWR, 0644);
    struct stat fi;
    fstat(fd, &fi);
    /// calculate number of records based on file size
    /// and size of the record.
    size_t size = fi.st_size / RECORD_SIZE;
    /// check if id more than number of companies.
    if (size < id) {
        printf("\nCompany with id %d does not exist.\n", id);
        close(fd);
        return;
    }
    lseek(fd, (id - 1) * RECORD_SIZE, SEEK_SET);
    char idstring[INT_FIELD_SIZE];
    read(fd, idstring, INT_FIELD_SIZE);
    /// check if company has been deleted.
    if (idstring[0] == '\0') {
        printf("\nCompany with id %d does not exist.\n", id);
        close(fd);
        return;
    }
    lseek(fd, (id - 1) * RECORD_SIZE, SEEK_SET);
    file_write_company(fd, id, name, city, employees);
    close(fd);
}

/// @brief Finds a company in a file and prints it's info.
/// @param id ID of the company to print.
void file_print_company(int id) {
    int fd = open(FILE_PATH, O_CREAT|O_RDONLY, 0644);
    struct stat fi;
    fstat(fd, &fi);
    size_t size = fi.st_size / RECORD_SIZE;
    if (size < id) {
        printf("\nCompany with id %d does not exist.\n", id);
        close(fd);
        return;
    }
    char idstring[INT_FIELD_SIZE];
    char name[STRING_FIELD_SIZE];
    char city[STRING_FIELD_SIZE];
    char employees[INT_FIELD_SIZE];
    lseek(fd, (id - 1) * RECORD_SIZE, SEEK_SET);
    read(fd, idstring, INT_FIELD_SIZE);
    read(fd, name, STRING_FIELD_SIZE);
    read(fd, city, STRING_FIELD_SIZE);
    read(fd, employees, INT_FIELD_SIZE);
    /// check if company has been deleted.
    if (idstring[0] == '\0') {
        printf("\nCompany with id %d does not exist.\n", id);
        close(fd);
        return;
    }
    printf("\nID: %s\nName: %s\nCity: %s\nEmployees: %s\n", idstring, name, city, employees);
    close(fd);
}

/// @brief Prints all companies in a file.
void file_print_all() {
    int fd = open(FILE_PATH, O_CREAT|O_RDONLY, 0644);
    struct stat fi;
    fstat(fd, &fi);
    int size = fi.st_size / RECORD_SIZE;
    char idstring[INT_FIELD_SIZE];
    char name[STRING_FIELD_SIZE];
    char city[STRING_FIELD_SIZE];
    char employees[INT_FIELD_SIZE];
    for (int i = 0; i < size; i++) {
        read(fd, idstring, INT_FIELD_SIZE);
        read(fd, name, STRING_FIELD_SIZE);
        read(fd, city, STRING_FIELD_SIZE);
        read(fd, employees, INT_FIELD_SIZE);
        if (idstring[0] == '\0') continue;
        printf("\nID: %s\nName: %s\nCity: %s\nEmployees: %s\n", idstring, name, city, employees);
    }
    close(fd);
}

/// @brief Writes company info in a file at the current position of the cursor.
/// @param fd File to which data will be written with cursor set.
/// @param id ID of the company.
/// @param name Name of the company.
/// @param city Location (city) of the company.
/// @param employees Number of employees of the company.
void file_write_company(int fd, int id, char* name, char* city, char* employees) {
    /// create buffers with fixed size, copy fields using
    /// strncat func for overflow safety.
    char id_buf[INT_FIELD_SIZE];
    sprintf(id_buf, "%d", id);
    /// id == 0 means no data.
    if (id == 0) {
        id_buf[0] = '\0';
    }
    char name_buf[STRING_FIELD_SIZE];
    name_buf[0] = '\0';
    strncat(name_buf, name, STRING_FIELD_SIZE);
    char city_buf[STRING_FIELD_SIZE];
    city_buf[0] = '\0';
    strncat(city_buf, city, STRING_FIELD_SIZE);
    char employees_buf[INT_FIELD_SIZE];
    employees_buf[0] = '\0';
    strncat(employees_buf, employees, INT_FIELD_SIZE);

    /// fill unused bytes with zero bytes.
    for (int i = 1; i < INT_FIELD_SIZE; i++) {
        if (id_buf[i - 1] == '\0') {
            id_buf[i] = '\0';
        }
        if (employees_buf[i - 1] == '\0') {
            employees_buf[i] = '\0';
        }
    }
    for (int i = 1; i < STRING_FIELD_SIZE; i++) {
        if (name_buf[i - 1] == '\0') {
            name_buf[i] = '\0';
        }   
        if (city_buf[i - 1] == '\0') {
            city_buf[i] = '\0';
        }
    }

    /// write data with fixed size fields.
    write(fd, id_buf, INT_FIELD_SIZE);
    write(fd, name_buf, STRING_FIELD_SIZE);
    write(fd, city_buf, STRING_FIELD_SIZE);
    write(fd, employees_buf, INT_FIELD_SIZE);
}

/// @brief Deletes a company in a file by rewriting it's record with nulls.
/// @param id ID of the company to delete.
void file_delete_company(int id) {
    int fd = open(FILE_PATH, O_CREAT|O_RDWR, 0644);
    struct stat fi;
    fstat(fd, &fi);
    size_t size = fi.st_size / RECORD_SIZE;
    if (size < id) {
        printf("\nCompany with id %d does not exist\n", id);
        close(fd);
        return;
    }
    lseek(fd, (id - 1) * RECORD_SIZE, SEEK_SET);
    char idstring[INT_FIELD_SIZE];
    read(fd, idstring, INT_FIELD_SIZE);
    /// check if company has already been deleted.
    if (idstring[0] == '\0') {
        printf("\nCompany with id %d does not exist\n", id);
        close(fd);
        return;
    }
    lseek(fd, (id - 1) * RECORD_SIZE, SEEK_SET);
    
    file_write_company(fd, 0, "\0", "\0", "\0");
    close(fd);
}

/// @brief Find and print all companies with most employees.
void file_print_most_employed() {
    int fd = open(FILE_PATH, O_CREAT|O_RDONLY, 0644);
    struct stat fi;
    fstat(fd, &fi);
    int size = fi.st_size / RECORD_SIZE;
    if (size == 0) {
        printf("\nNo companies in the database.\n");
        close(fd);
        return;
    }
    char employees[INT_FIELD_SIZE];
    int maxEmployees = -1;
    int currEmpoyees = -1;
    /// find highest number of employees among all companies.
    for (int i = 0; i < size; i++) {
        lseek(fd, i * RECORD_SIZE + (RECORD_SIZE - INT_FIELD_SIZE), SEEK_SET);
        read(fd, employees, INT_FIELD_SIZE);
        if (employees[0] == '\0') continue;
        currEmpoyees = atoi(employees);
        if (currEmpoyees > maxEmployees) {
            maxEmployees = currEmpoyees;
        }
    }
    if (maxEmployees == -1) {
        printf("\nNo companies in the database.\n");
        close(fd);
        return;
    }
    /// set file cursor at the start of the file.
    lseek(fd, 0, SEEK_SET);
    printf("\nMost employed companies:\n");
    char idstring[INT_FIELD_SIZE];
    char name[STRING_FIELD_SIZE];
    char city[STRING_FIELD_SIZE];
    /// iterate over all companies employees count, if that number
    /// matches highest number of employees, then read and print
    /// company info.
    for (int i = 0; i < size; i++) {
        lseek(fd, i * RECORD_SIZE + (RECORD_SIZE - INT_FIELD_SIZE), SEEK_SET);
        read(fd, employees, INT_FIELD_SIZE);
        if (employees[0] == '\0') continue;
        currEmpoyees = atoi(employees);
        if (currEmpoyees == maxEmployees) {
            lseek(fd, i * RECORD_SIZE, SEEK_SET);
            read(fd, idstring, INT_FIELD_SIZE);
            read(fd, name, STRING_FIELD_SIZE);
            read(fd, city, STRING_FIELD_SIZE);
            printf("\nID: %s\nName: %s\nCity: %s\nEmployees: %s\n",
                idstring, name, city, employees);
        }
    }
    close(fd);
}

/// @brief Print number of unique cities in which companies are located.
void file_print_cities_count() {
    int fd = open(FILE_PATH, O_CREAT|O_RDONLY, 0644);
    struct stat fi;
    fstat(fd, &fi);
    int size = fi.st_size / RECORD_SIZE;
    if (size == 0) {
        printf("\nNo companies in the database.\n");
        close(fd);
        return;
    }
    char city[STRING_FIELD_SIZE];
    char tmp_city[STRING_FIELD_SIZE];
    int cities = 0;
    for (int i = 0; i < size; i++) {
        /// read current company city name.
        lseek(fd, i * RECORD_SIZE + INT_FIELD_SIZE + STRING_FIELD_SIZE, SEEK_SET);
        read(fd, city, STRING_FIELD_SIZE);
        if (city[0] == '\0') continue;
        int found = 0;
        /// iterate over all companies before current one and compare their city names
        /// with current company city name. 
        for (int j = 0; j < i; j++) {
            lseek(fd, j * RECORD_SIZE + INT_FIELD_SIZE + STRING_FIELD_SIZE, SEEK_SET);
            read(fd, tmp_city, STRING_FIELD_SIZE);
            if (strcasecmp(city, tmp_city) == SAME) {
                found = 1;
                break;
            }
        }
        /// if city with current name wasn't found before, then increment cities counter.
        if (!found) {
            cities++;
        }
    }
    printf("\nCompanies are in %d cities.\n", cities);
    close(fd);
}