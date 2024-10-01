#include <stdio.h>
#include <unistd.h>
#include <sys/wait.h>
#include <stdlib.h>

#include "constants.h"
#include "input.h"

/// @breaf Takes input from a user and then starts child process and passes
/// to input for processing.
int main() {
    char input1[DEFAULT_BUFF_SIZE];
    input_take_string(input1, DEFAULT_BUFF_SIZE, "Enter first value: ");
    char input2[DEFAULT_BUFF_SIZE];
    input_take_string(input2, DEFAULT_BUFF_SIZE, "Enter second value: ");

    pid_t childPid;
    char path[] = "child.out";
    char* args[] = {path, input1, input2, NULL};
    childPid = fork();
    if (childPid == 0) {
        execv(path, args);
        printf("failed to run process\n");
        return EXIT_FAILURE;
    } else {
        wait(NULL);
    }
}
