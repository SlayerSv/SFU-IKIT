#include "process_values.h"

void processValues(char* output, char* input1, int input1_size, char* input2, int input2_size) {
    char* end1;
    char* end2;
    long long num1 = strtoll(input1, &end1, 10);
    long long num2 = strtoll(input2, &end2, 10);
    if (*end1 == '\0' && *end2 == '\0' && input1_size > 0 && input2_size > 0) {
        sprintf(output, "%lld", num1 % num2);
        return;
    }
    double num3 = strtod(input1, &end1);
    double num4 = strtod(input2, &end2);
    if (*end1 == '\0' && *end2 == '\0' && input1_size > 0 && input2_size > 0) {
        sprintf(output, "%f", num3 + num4);
        return;
    }
    strcpy(output, input1);
    strcat(output, input2);
}