#include <fcntl.h>
#include <stdio.h>
#include <sys/stat.h>
#include <stdlib.h>

#include "company.h"
#include "input.h"
#include "companyfile.h"

#define INT_STRING_SIZE 9
#define FILE_PATH "db.txt"
const int RECORD_SIZE = INT_STRING_SIZE * 2 + MAX_STRING_SIZE * 2;

void file_add_company(char* name, char* city, char* employees) {
    int fd = open(FILE_PATH, O_CREAT|O_APPEND|O_WRONLY, 0644);
    struct stat fi;
    fstat(fd, &fi);
    int id = fi.st_size / RECORD_SIZE + 1;
    char id_string[INT_STRING_SIZE];
    sprintf(id_string, "%d", id);
    file_write_company(fd, id_string, name, city, employees);
    close(fd);
}

void file_update_company(int id, char* name, char* city, char* employees) {
    int fd = open(FILE_PATH, O_CREAT|O_RDWR, 0644);
    struct stat fi;
    fstat(fd, &fi);
    size_t size = fi.st_size / RECORD_SIZE;
    if (size < id) {
        printf("Company with id %d does not exist", id);
        return;
    }
    lseek(fd, (id - 1) * RECORD_SIZE, SEEK_SET);
    char idstring[INT_STRING_SIZE];
    read(fd, idstring, INT_STRING_SIZE);
    if (idstring[0] == '\0') {
        printf("Company with id %d does not exist", id);
        return;
    }
    lseek(fd, (id - 1) * RECORD_SIZE, SEEK_SET);
    file_write_company(fd, idstring, name, city, employees);
    close(fd);
}

void file_print_company(int id) {
    int fd = open(FILE_PATH, O_CREAT|O_RDONLY, 0644);
    struct stat fi;
    fstat(fd, &fi);
    size_t size = fi.st_size / RECORD_SIZE;
    if (size < id) {
        printf("Company with id %d does not exist", id);
        return;
    }
    char idstring[INT_STRING_SIZE];
    char name[MAX_STRING_SIZE];
    char city[MAX_STRING_SIZE];
    char employees[INT_STRING_SIZE];
    lseek(fd, (id - 1) * RECORD_SIZE, SEEK_SET);
    read(fd, idstring, INT_STRING_SIZE);
    read(fd, name, MAX_STRING_SIZE);
    read(fd, city, MAX_STRING_SIZE);
    read(fd, employees, INT_STRING_SIZE);
    if (idstring[0] == '\0') {
        printf("Company with id %d does not exist", id);
        return;
    }
    printf("ID: %s\nName: %s\nCity: %s\nEmployees: %s\n", idstring, name, city, employees);
    close(fd);
}

void file_print_all() {
    int fd = open(FILE_PATH, O_CREAT|O_RDONLY, 0644);
    struct stat fi;
    fstat(fd, &fi);
    int size = fi.st_size / RECORD_SIZE;
    char idstring[INT_STRING_SIZE];
    char name[MAX_STRING_SIZE];
    char city[MAX_STRING_SIZE];
    char employees[INT_STRING_SIZE];
    for (int i = 0; i < size; i++) {
        read(fd, idstring, INT_STRING_SIZE);
        read(fd, name, MAX_STRING_SIZE);
        read(fd, city, MAX_STRING_SIZE);
        read(fd, employees, INT_STRING_SIZE);
        if (idstring[0] == '\0') continue;
        printf("\nID: %s\nName: %s\nCity: %s\nEmployees: %s\n", idstring, name, city, employees);
    }
    close(fd);
}

void file_write_company(int fd, char* id, char* name, char* city, char* employees) {
    for (int i = 1; i < INT_STRING_SIZE; i++) {
        if (id[i - 1] == '\0') id[i] = '\0';
        if (employees[i - 1] == '\0') employees[i] = '\0';
    }
    for (int i = 1; i < MAX_STRING_SIZE; i++) {
        if (name[i - 1] == '\0') name[i] = '\0';
        if (city[i - 1] == '\0') city[i] = '\0';
    }
    write(fd, id, INT_STRING_SIZE);
    write(fd, name, MAX_STRING_SIZE);
    write(fd, city, MAX_STRING_SIZE);
    write(fd, employees, INT_STRING_SIZE);
}

void file_delete_company(int id) {
    int fd = open(FILE_PATH, O_CREAT|O_RDWR, 0644);
    struct stat fi;
    fstat(fd, &fi);
    size_t size = fi.st_size / RECORD_SIZE;
    if (size < id) {
        printf("Company with id %d does not exist", id);
        return;
    }
    lseek(fd, (id - 1) * RECORD_SIZE, SEEK_SET);
    char idstring[INT_STRING_SIZE];
    read(fd, idstring, INT_STRING_SIZE);
    if (idstring[0] == '\0') {
        printf("Company with id %d does not exist", id);
        return;
    }
    lseek(fd, (id - 1) * RECORD_SIZE, SEEK_SET);
    idstring[0] = '\0';
    char name[MAX_STRING_SIZE] = {'\0'};
    char city[MAX_STRING_SIZE] = {'\0'};
    char employees[INT_STRING_SIZE] = {'\0'};
    file_write_company(fd, idstring, name, city, employees);
}