#include "process_values.h"

void processValues(char* result, char* input1, char* input2) {
    char* end1;
    char* end2;
    int len1 = strlen(input1);
    int len2 = strlen(input2);
    long long num1 = strtoll(input1, &end1, 10);
    long long num2 = strtoll(input2, &end2, 10);
    if (*end1 == '\0' && *end2 == '\0' && len1 > 0 && len2 > 0) {
        sprintf(result, "%lld", num1%num2);
        return;
    }
    double num3 = strtod(input1, &end1);
    double num4 = strtod(input2, &end2);
    if (*end1 == '\0' && *end2 == '\0' && len1 > 0 && len2 > 0) {
        sprintf(result, "%f", num3+num4);
        return;
    }
    strcpy(result, input1);
    strcat(result, input2);
}