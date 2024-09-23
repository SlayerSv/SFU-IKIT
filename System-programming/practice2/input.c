#include "input.h"
#include "constants.h"

void input_take_uint(int* val, char* message) {
    char input[DEFAULT_BUFF_SIZE];
    int c;
    while (1) {
        printf("%s", message);
        gets(input);
        if (strlen(input) > MAX_INT_SIZE) {
            printf("Input too long.\n");
            continue;
        }
        char* end;
        *val = strtol(input, &end, 10);
        if (*end == '\0' && *val > 0) {
            return;
        }
        printf("Must provide a positive integer.\n");
    }
}