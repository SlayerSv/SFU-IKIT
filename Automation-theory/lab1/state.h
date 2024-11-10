#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#define UNKNOWN_SYMBOL_ERR -1
#define NOT_FINAL_STATE     0
#define FINAL_STATE         1

#define FINAL_STATES        1

#define TOTAL_STATES            5

#define NUMBER_OF_LETTERS       53
#define NUMBER_OF_DIGITS        10
#define NUMBER_OF_CHAR_TYPES    3
#define MAX_CHARS 31

struct States {
    struct State* head;
    struct State* tail;
    int size;
    char* word;
    int word_length;
};

struct State {
    int symbol_index;
    int state_number;
    struct State* next;
    struct State* prev;
};

struct State* state_new(int curr_symbol_index, int curr_state_number);
void states_push(struct States* states, struct State* new_state);
struct State* states_pop(struct States* states);
void states_print(struct States* states);
struct States* states_new(char* word);
void SetDFA_Transitions();
void next_step(struct States* states);