#include "input.h"

/// @brief Requests input from a user until he provides a valid positive integer.
/// @param val Pointer to a value to which valid integer will be saved.
/// @param message Message that will be prompted to a user before taking input.
void input_take_uint(int* val, char* message) {
    const int buffer_size = MAX_INT_SIZE + 1;
    char input[buffer_size];
    input[buffer_size - 1] = '\0';
    int pos, c;
    while (1) {
        printf("%s", message);
        fgets(input, buffer_size, stdin);
        /// find how many characters are read until new line is encountered.
        pos = strcspn(input, "\n");
        /// if characters read equal to buffers size - 1 that means new line
        /// hasn't been encountered, which means input exceeds input buffer size.
        if (pos == buffer_size - 1) {
            printf("Input too long.\n");
            /// clear buffer.
            while ((c = getchar()) != '\n' && c != EOF) {}
            continue;
        } else {
            /// replace new line with terminating null.
            input[pos] = '\0';
        }
        char* end;
        *val = strtol(input, &end, 10);
        /// check that value entered is a positive integer.
        if (*end == '\0' && *val > 0 && pos > 0) {
            return;
        }
        printf("Must provide a positive integer.\n");
    }
}