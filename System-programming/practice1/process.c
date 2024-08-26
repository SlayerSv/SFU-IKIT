# include <stdio.h>
# include <stdlib.h>
# include <string.h>

void processValues(char* input1, char* input2) {
    char* end1;
    char* end2;
    long long num1 = strtoll(input1, &end1, 10);
    long long num2 = strtoll(input2, &end2, 10);
    if (*end1 == '\0' && *end2 == '\0') {
        printf("Remainder of %lld divided by %lld is %lld\n", num1, num2, num1%num2);
        return;
    }
    double num3 = strtod(input1, &end1);
    double num4 = strtod(input2, &end2);
    if (*end1 == '\0' && *end2 == '\0') {
        printf("Sum of %f and %f is %f\n", num3, num4, num3+num4);
        return;
    }
    int len1 = strlen(input1);
    int len2 = strlen(input2);
    int totalLen = len1 + len2 + 1;
    char output[totalLen];
    strcpy(output, input1);
    strcat(output, input2);
    printf("Concatinated string is %s\n", output);
}

int main() {
    printf("Enter first value: ");
    char input1[100];
    scanf("%s", input1);
    printf("Enter second value: ");
    char input2[100];
    scanf("%s", input2);
    processValues(input1, input2);
}

int isInteger(char* value) {
    int i = 0;
    if (value[0] == '+' || value[0] == '-') {
        i++;
    }
    if (value[0] == '\0') {
        return 0;
    }
    while (value[i] != '\0') {
        if (value[i] < '0' || value[i] > '9') {
            return 0;
        }
    }
    return 1;
}

int isDecimal(char* value) {
    int i = 0;
    if (value[0] == '+' || value[0] == '-') {
        i++;
    }
    if (value[0] == '\0') {
        return 0;
    }
}