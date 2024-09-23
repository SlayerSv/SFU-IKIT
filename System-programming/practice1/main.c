#include <stdio.h>
#include <unistd.h>
#include <sys/wait.h>

#include "constants.h"

int main() {
    printf("Enter first value: ");
    char input1[DEFAULT_BUFF_SIZE];
    scanf("%s", input1);
    printf("Enter second value: ");
    char input2[DEFAULT_BUFF_SIZE];
    scanf("%s", input2);

    pid_t childPid;
    char path[] = "child.out";
    char* args[] = {path, input1, input2, NULL};
    childPid = fork();
    if (childPid == 0) {
        execv(path, args);
        printf("failed to run process\n");
        return 1;
    } else {
        wait(NULL);
    }
}
