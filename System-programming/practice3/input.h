#ifndef INPUT
#define INPUT

#include <stdio.h>

#define MAX_STRING_SIZE 30
#define MIN_STRING_SIZE 2
#define VALID 0

void input_take_string(char input[], char* message);
void input_take_uint(int* val, char* message);
int input_check_string(char* input);

#endif