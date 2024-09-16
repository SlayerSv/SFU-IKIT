#include <stdio.h>
#include <ctype.h>
#include <string.h>
#include <stdlib.h>

#include "input.h"

void input_take_string(char* input, char* message) {
    int c;
    int valid = 1;
    while (1) {
        printf("%s", message);
        scanf("%s", input);
        while ((c = getchar()) != '\n' && c != EOF ) {}
        if (VALID == input_check_string(input)) break;
    }
}

void input_take_uint(unsigned int* val, char* message) {
    char input[10];
    int c;
    while (1) {
        printf("%s", message);
        scanf("%s", input);
        char* end;
        *val = strtoul(input, &end, 10);
        while ((c = getchar()) != '\n' && c != EOF ) {}
        if (*end == '\0') {
            return;
        }
        printf("Must provide a positive integer.\n");
    }
}

int input_check_string(char* input) {
    int valid, c;
    size_t size = strlen(input);
    valid = 0;
    if (size > 30) {
        printf("Max %d characters are allowed.\n", MAX_STRING_SIZE);
        valid = -1;
    }
    if (size < 2) {
        printf("Min %d characters are needed.\n", MIN_STRING_SIZE);
        valid = -1;
    }
    int i = 0;
    c = input[i];
    while (c != '\0') {
        if (!isalpha(c)) {
            printf("Only characters [A-Z][a-z] are allowed.\n");
            valid = -1;
            break;
        }
        c= input[++i];
    }
    return valid;
}