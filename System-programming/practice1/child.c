#include "process_values.h"
#include "constants.h"

/// \brief Accepts 2 arguments from CLI and calls processing function,
/// writes the result to stdin.
int main(int argc, char* argv[]) {
    if (argc != 3) {
        printf("Must be exactly 2 arguments. Provided: %d\n", argc - 1);
        return 1;
    }
    int buffsize = DEFAULT_BUFF_SIZE;
    int len1 = strlen(argv[1]);
    int len2 = strlen(argv[2]);
    if (len1 + len2 + 1 > buffsize) {
        buffsize = len1 + len2 + 1;
    };
    char output[buffsize];
    processValues(output, argv[1], len1, argv[2], len2);
    printf("%s\n", output);
}