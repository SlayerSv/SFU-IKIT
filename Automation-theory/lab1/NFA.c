#include <stdio.h> 
#include <string.h>
#include "state.h"

void input_take_string(char* input, int buffer_size, char* message);

int main(void) {
    SetDFA_Transitions();    // Fill transition table
    char current_symbol;
    char user_input[MAX_CHARS];
    input_take_string(user_input, MAX_CHARS, "Enter a string with any alphanumeric characters: ");
    int accepted = 0;
    struct States* states = states_new(user_input);
    struct State* initial_state = state_new(0, 0); 
    states_push(states, initial_state);
    int steps_counter = 0;
    while (states->size > 0) {
        printf("\nStep %d:\n", steps_counter++);
        states_print(states);
        accepted = next_step(states);
    }
    if (accepted) {
        printf("\nAccepted: %s\n", user_input);
    } else {
        printf("\nRejected: %s\n", user_input);
    }
    return 0;
}

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
            printf("Input cannot exceed %d.\n", MAX_CHARS);
            /// clear buffer.
            while ((c = getchar()) != '\n' && c != EOF) {}
            continue;
        }
        /// replace new line with terminating null.
        input[pos] = '\0';
        break;
    }
}

