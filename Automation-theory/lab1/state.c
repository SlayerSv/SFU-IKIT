#include "state.h"

enum DFA_STATES {q0, q1, q2, q3, q4, end, error};
int  g_Accepted_states[FINAL_STATES] = {q4};

int g_state0[2] = {q0, end};
int g_state1and2[3] = {q1, q2, end};
int g_state2[2] = {q2, end};
int g_state3[2] = {q3, end};
int g_state4[2] = {q4, end};
int g_empty_state[1] = {end};
int g_error_state[2] = {error, end};
enum char_Types{letter, digit, empty};

char g_Letters[NUMBER_OF_LETTERS] = {'_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
    'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
    'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
char g_Digits[NUMBER_OF_DIGITS] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
int*  g_Transition_Table[TOTAL_STATES][NUMBER_OF_CHAR_TYPES] = {}; // Transition function


void SetDFA_Transitions() {
    g_Transition_Table[q0][letter] = g_empty_state;
    g_Transition_Table[q0][digit] = g_empty_state;
    g_Transition_Table[q0][empty] = g_state1and2;
    g_Transition_Table[q1][letter] = g_error_state;
    g_Transition_Table[q1][digit] = g_state3;
    g_Transition_Table[q1][empty] = g_empty_state;
    g_Transition_Table[q2][letter] = g_state4;
    g_Transition_Table[q2][digit] = g_error_state;
    g_Transition_Table[q2][empty] = g_empty_state;
    g_Transition_Table[q3][letter] = g_state3;
    g_Transition_Table[q3][digit] = g_state3;
    g_Transition_Table[q3][empty] = g_empty_state;
    g_Transition_Table[q4][letter] = g_state4;
    g_Transition_Table[q4][digit] = g_state4;
    g_Transition_Table[q4][empty] = g_empty_state;
}

int* nextState(int state, const char current_symbol) {
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
    if (current_symbol == '\0') {
        pos = empty;
    };
    if (pos == NUMBER_OF_CHAR_TYPES) {
        return g_error_state;
    }
    return g_Transition_Table[state][pos];
}

int next_step(struct States* states) {
    int size = states->size;
    int* next_states;
    struct State* curr_state;
    struct State* new_state;
    int result = 0;
    for (int i = 0; i < size; i++) {
        curr_state = states_pop(states);
        if (curr_state->state_number == error) {
            free(curr_state);
            continue;
        }
        if (curr_state->symbol_index == states->word_length &&
            isFinalState(curr_state->state_number)) {
                result = 1;
            }
        next_states = nextState(curr_state->state_number, '\0');
        for (int j = 0; j < TOTAL_STATES; j++) {
            if (next_states[j] == end) {
                break;
            }
            new_state = state_new(curr_state->symbol_index, next_states[j]);
            states_push(states, new_state);
        }
        if (curr_state->symbol_index == states->word_length) {
            free(curr_state);
            continue;
        }
        next_states = nextState(curr_state->state_number, states->word[curr_state->symbol_index]);
        for (int j = 0; j < TOTAL_STATES; j++) {
            if (next_states[j] == end) {
                break;
            }
            new_state = state_new(curr_state->symbol_index + 1, next_states[j]);
            states_push(states, new_state);
        }
        free(curr_state);
    }
    return result;
}


struct State* state_new(int curr_symbol_index, int curr_state_number) {
    struct State* new_state = malloc(sizeof(struct State));
    new_state->state_number = curr_state_number;
    new_state->symbol_index = curr_symbol_index;
    return new_state;
}

void states_push(struct States* states, struct State* new_state) {
    states->tail->prev->next = new_state;
    new_state->prev = states->tail->prev;
    new_state->next = states->tail;
    states->tail->prev = new_state;
    states->size++;
}

struct State* states_pop(struct States* states) {
    if (states->size == 0) {
        return NULL;
    }
    struct State* state = states->head->next;
    state->prev->next = state->next;
    state->next->prev = state->prev;
    states->size--;
    return state;
}

struct States* states_new(char* word) {
    struct States* states = malloc(sizeof(struct States));
    states->word = word;
    states->word_length = strlen(word);
    states->size = 0;
    states->head = malloc(sizeof(struct State));
    states->tail = malloc(sizeof(struct State));
    states->head->next = states->tail;
    states->tail->prev = states->head;
    states->head->state_number = -1;
    states->tail->state_number = -1;
    return states; 
}

int isFinalState(int state) {
    if (state == end) {
        return UNKNOWN_SYMBOL_ERR;
    }
    for (int i = 0; i < FINAL_STATES; ++i) {
        if (state == g_Accepted_states[i]) {
            return FINAL_STATE;
        }
    }
    return NOT_FINAL_STATE;
}

void states_print(struct States* states) {
    struct State* state = states->head->next;
    for (int i = 0; i < states->size; i++) {
        for (int j = 0; j < state->symbol_index; j++) {
            printf("%c", states->word[j]);
        }
        if (state->state_number == error) {
            printf(": transition failed\n");
            state = state->next;
            continue;
        }
        printf(": q%d, ", state->state_number);
        
        if (isFinalState(state->state_number)) {
            printf("Final state\n");
        } else {
            printf("Not final state\n");
        }
        state = state->next;
    }
}