#ifndef INPUT
#define INPUT

#include <stdio.h>

void input_take_string(char* input, int buffer_size, char* message);
void input_take_uint(int* val, char* message);
int input_check_string(char* input);

#endif