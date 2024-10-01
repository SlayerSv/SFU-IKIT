#include "input.h"

/// @brief Requests input from a user until he provides a valid string.
/// @param input Buffer to which string will be written.
/// @param buffer_size Buffer size of input param.
/// @param message essage that will be prompted to a user before taking input.
void input_take_string(char* input, int buffer_size, char* message) {
    int c, pos;
    int valid = 1;
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
        break;
    }
}