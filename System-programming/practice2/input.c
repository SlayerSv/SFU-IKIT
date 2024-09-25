#include "input.h"

void input_take_uint(int* val, char* message) {
    const int buffer_size = MAX_INT_SIZE;
    char input[buffer_size];
    input[buffer_size - 1] = '\0';
    int pos, c;
    while (1) {
        printf("%s", message);
        fgets(input, buffer_size, stdin);
        pos = strcspn(input, "\n");
        if (pos == buffer_size - 1) {
            printf("Input too long.\n");
            while ((c = getchar()) != '\n' && c != EOF) {}
            continue;
        } else {
            input[pos] = '\0';
        }
        char* end;
        *val = strtol(input, &end, 10);
        if (*end == '\0' && *val > 0 && pos > 0) {
            return;
        }
        printf("Must provide a positive integer.\n");
    }
}