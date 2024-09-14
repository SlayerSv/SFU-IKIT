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
        size_t size = strlen(input);
        valid = 1;
        if (size > 30) {
            printf("Max %d characters are allowed.\n", MAX_STRING_SIZE);
            valid = 0;
        }
        if (size < 2) {
            printf("Min %d characters are needed.\n", MIN_STRING_SIZE);
            valid = 0;
        }
        c = input[0];
        while (c != '\0') {
            if (!isalpha(c)) {
                printf("Only characters [A-Z][a-z] are allowed.\n");
                valid = 0;
                break;
            }
        }
        while ((c = getchar()) != '\n' && c != EOF ) {}
        if (valid) break;
    }
}

void input_take_uint(unsigned int val, char* message) {
    char input[10];
    int c;
    while (1) {
        printf("%s", message);
        scanf("%s", input);
        char* end;
        val = strtoul(input, &end, 10);
        while ((c = getchar()) != '\n' && c != EOF ) {}
        if (*end == '\0') {
            return;
        }
        printf("Must provide a positive integer.\n");
    }
}