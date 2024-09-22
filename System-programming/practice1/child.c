#include <stdio.h>
#include "process_values.h"

int main(int argc, char* argv[]) {
    if (argc != 3) {
        printf("Must be exactly 2 arguments. Provided: %d\n", argc - 1);
        return 1;
    }
    char output[200];
    processValues(output, argv[1], argv[2]);
    printf("%s\n", output);
}