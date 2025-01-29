#include <stdio.h>
#include <stdlib.h>

#include "recursive_descent_parser.h"

const int MAX_INPUT_LENGTH = 1000;
char INPUT[MAX_INPUT_LENGTH];
const int MIN_VARIABLE_NAME_LENGTH = 1;
const int MAX_VARIABLE_NAME_LENGTH = 2;

char letters[] = {'a', 'b', 'c', 'd', '\0'};
char digits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '\0'};
char operations[] = {'+', '*', '\0'};

int i = 0;
int input_length;
char token[MAX_INPUT_LENGTH + 1];
int BEGIN = 1;
int END = 0;

int main() {
    printf("Enter expressions to parse: ");
    input_length = fread(INPUT, 1, MAX_INPUT_LENGTH, stdin);

}

void start() {
    get_token();
    int j = 0;
    if (!find(token[j], letters)) {
        printf("Expression must start with a varible name\n");
    }
    parse_variable();
    get_token();
    if (token[0] != '=') {
        printf("ERROR: expected '=', got %s\n", token);
        exit(EXIT_FAILURE);
    }
    get_token();
    parse_operand();
}

void skip_white_space() {
    while (i < input_length && is_space(INPUT[i++])) {}
}

int find(char ch, char* arr) {
    int j = 0;
    while (arr[j] != '\0') {
        if (ch == arr[j++]) {
            return 1;
        }
    }
    return 0;
}

void parse_operand() {
    int j = 0;
    int bracket = 0;
    if (token[j] == '-') {
        j++;
        if token[j]
    }
    if (token[j] == '(') {
        parse_bracket_expression();
    } else if (find(token[0], letters)) {
        parse_variable();
    } else if (find(token[0], digits)) {
        parse_number();
    } else {
        printf()
    }
}

void parse_number() {

}

void parse_variable() {
    int length = 0, j = 0;
    while (token[j] != '\0') {
        if (!find(token[j], letters)) {
            printf("ERROR: Unexpected symbol %c in variable name %s", token[j], token);
            printf("Allowed characters in variable names are: ");
            int k = 0;
            while (letters[k] != '\0') {
                printf("'%c '", letters[k++]);
            }
            exit(EXIT_FAILURE);
        }
        j++;
        length++;
    }
    if (length < MIN_VARIABLE_NAME_LENGTH || length > MAX_VARIABLE_NAME_LENGTH) {
        printf("ERROR: varible name length must be between %d and %d",
            MIN_VARIABLE_NAME_LENGTH, length > MAX_VARIABLE_NAME_LENGTH);
        exit(EXIT_FAILURE);
    }
    get_token();
    parse_operation();
}

void parse_sign() {

}

void parse_operation() {

}

int is_space(char ch) {
    if (ch == ' ' || ch == '\n' || ch == '\t') {
        return 1;
    }
    return 0;
}

void get_token() {
    int j = 0;
    skip_white_space();
    while (i < input_length && !is_space(INPUT[i])) {
        token[j++] = INPUT[i++];
    }
    token[j] = '\0';
}

void parse_bracket_expression() {

}