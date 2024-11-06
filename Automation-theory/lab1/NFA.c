#include <stdio.h> 
#include <string.h>

#define TOTAL_STATES            3
#define FINAL_STATES            1
#define NUMBER_OF_LETTERS       53
#define NUMBER_OF_DIGITS        10
#define NUMBER_OF_CHAR_TYPES    2
#define MAX_CHARS 31

#define UNKNOWN_SYMBOL_ERR -1
#define NOT_FINAL_STATE     1
#define FINAL_STATE         2

enum DFA_STATES {q0, q1, q2};   // The set Q
enum char_Types{letter, digit};
int  g_Accepted_states[FINAL_STATES] = {q1};
char g_Letters[NUMBER_OF_LETTERS] = {'_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
    'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
    'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
char g_Digits[NUMBER_OF_DIGITS] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
int  g_Transition_Table[TOTAL_STATES][NUMBER_OF_CHAR_TYPES] = {}; // Transition function

void SetDFA_Transitions() {
    g_Transition_Table[q0][letter] = q1;
    g_Transition_Table[q0][digit] = q2;
    g_Transition_Table[q1][letter] = q1;
    g_Transition_Table[q1][digit] = q1;
    g_Transition_Table[q2][letter] = q2;
    g_Transition_Table[q2][digit] = q2;
}

int nextState(int state, const char current_symbol) {
    int pos = NUMBER_OF_CHAR_TYPES;
    for (int i = 0; i < NUMBER_OF_LETTERS; i++) {
        if (current_symbol == g_Letters[i]) {
            pos = letter;
            break;
        }
    }
    for (int i = 0; i < NUMBER_OF_DIGITS; i++) {
        if (current_symbol == g_Digits[i]) {
            pos = digit;
            break;
        }
    }
    if (NUMBER_OF_CHAR_TYPES == pos) {
        return UNKNOWN_SYMBOL_ERR;
    }
   state = g_Transition_Table[state][pos];
   return state;
}

int isFinalState(int state) {
    for (int i = 0; i < FINAL_STATES; ++i) {
        if (state == g_Accepted_states[i]) {
            return FINAL_STATE;
        }
    }
    return NOT_FINAL_STATE;
}

void input_take_string(char* input, int buffer_size, char* message);

int main(void) {
    SetDFA_Transitions();    // Fill transition table
    char current_symbol;
    char user_input[MAX_CHARS];
    input_take_string(user_input, MAX_CHARS, "Enter a string with any alphanumeric characters: ");
    int i = 0;
    int result = 0;
    int state = q0;
    do {
        for (int j = 0; j < i; j++) {
            printf("%c", user_input[j]);
        }
        result = isFinalState(state);
        if (state == UNKNOWN_SYMBOL_ERR) {
            printf(": Unknown symbol. Stop\n");
            break;
        }
        if (result == FINAL_STATE) {
            printf(": q%d, Final state\n", state);
        } else {
            printf(": q%d, Not final state\n", state);
        }
        current_symbol = user_input[i++];
        state = nextState(state, current_symbol);
    } while (current_symbol != '\0');
    if (FINAL_STATE == result) {
        printf("Accepted: ");
    } else {
        printf("Rejected: ");
    }
    printf("%s\n", user_input);
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