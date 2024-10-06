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
        if (VALID == input_check_string(input)) break;
    }
}

/// @brief Requests input from a user until he provides a valid positive integer.
/// @param val Pointer to a value to which valid integer will be saved.
/// @param message Message that will be prompted to a user before taking input.
void input_take_uint(int* val, char* message) {
    const int buffer_size = MAX_INT_SIZE;
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
        if (*end == '\0' && *val >= 0 && pos > 0) {
            return;
        }
        printf("Must provide a non-negative integer.\n");
    }
}

/// @brief Check a string for validity by specific rules.
/// @param input String to check.
/// @return 0 if string is valid.
///         -1 otherwise.
int input_check_string(char* input) {
    int valid, c;
    size_t size = strlen(input);
    valid = 0;
    if (size < MIN_STRING_SIZE) {
        printf("Minimum %d characters are needed.\n", MIN_STRING_SIZE);
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