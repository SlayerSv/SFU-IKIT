#ifndef INPUT
#define INPUT

#include <stdio.h>

#define MAX_STRING_SIZE 30
#define MIN_STRING_SIZE 2

void input_take_string(char input[], char* message);
void input_take_uint(unsigned int* val, char* message);

#endif