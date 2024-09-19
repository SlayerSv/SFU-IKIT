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
        gets(input);
        if (VALID == input_check_string(input)) break;
    }
}

void input_take_uint(int* val, char* message) {
    char input[1000];
    int c;
    while (1) {
        printf("%s", message);
        gets(input);
        if (strlen(input) > 9) {
            printf("Input too long.\n");
            continue;
        }
        char* end;
        *val = strtol(input, &end, 10);
        if (*end == '\0' && *val >= 0) {
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
    if (c == ' ') {
        printf("Leading space is not allowed.\n");
        valid = -1;
    }
    while (c != '\0') {
        if (!isalpha(c) && c != ' ') {
            printf("Only characters [A-Z], [a-z], [ ] are allowed.\n");
            valid = -1;
            break;
        }
        if (c == ' ' && i > 0 && input[i - 1] == ' ') {
            printf("Only one consecutive space is allowed.\n");
            valid = -1;
            break;
        }
        c = input[++i];
    }
    if (i > 1 && input[i - 1] == ' ') {
        printf("Trailing space is not allowed.\n");
        valid = -1;
    }
    return valid;
}