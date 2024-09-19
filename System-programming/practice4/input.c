#include <stdio.h>
#include <ctype.h>
#include <string.h>
#include <stdlib.h>

#include "input.h"

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