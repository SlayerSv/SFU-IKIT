#include <stdio.h> 
#include <string.h>

#define TOTAL_STATES            5
#define FINAL_STATES            4
#define ALPHABET_CHARCTERS      2

#define MAX_CHARS 100

#define UNKNOWN_SYMBOL_ERR -1
#define NOT_FINAL_STATE     1
#define FINAL_STATE         2

enum DFA_STATES {q0, q1, q2, q3, q4};   // The set Q
enum input {_a, _b};

int  g_Accepted_states[FINAL_STATES] = {q1, q2, q3, q4};     // The set F
char g_alphabet[ALPHABET_CHARCTERS] = {'a', 'b'};  // The set Sigma
int  g_Transition_Table[TOTAL_STATES][ALPHABET_CHARCTERS] = {}; // Transition function

void SetDFA_Transitions() {
    g_Transition_Table[q0][_a] = q1;
    g_Transition_Table[q0][_b] = q1;
    g_Transition_Table[q1][_a] = q2;
    g_Transition_Table[q1][_b] = q2;
    g_Transition_Table[q2][_a] = q3;
    g_Transition_Table[q2][_b] = q3;
    g_Transition_Table[q3][_a] = q4;
    g_Transition_Table[q3][_b] = q4;
    g_Transition_Table[q4][_a] = q0;
    g_Transition_Table[q4][_b] = q0;
}

int nextState(int state, const char current_symbol) {
    int pos;
    for (pos = 0; pos < ALPHABET_CHARCTERS; ++pos) {
        if (current_symbol == g_alphabet[pos]) {
            break;
        }
    }
    if (ALPHABET_CHARCTERS == pos) {
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
    input_take_string(user_input, MAX_CHARS, "Enter a string with 'a's and 'b's: ");
    int i = 0;
    int result = 0;
    int state = q0;
    int фывыф = 4;
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
            printf(": Final state\n");
        } else {
            printf(": Not final state\n");
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