#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#include "input.h"

void usage() {
    printf("Usage:\n\
create [company name] [company city] [employee count] (create new company)\n\
update [company ID] [company name] [company city] [employee count] (update company)\n\
delete [company ID] (delete company)\n\
print [company ID] (print company)\n\
print --all, -a (print all companies)\n\
print --employees, -e (print companies with most employees)\n\
print --cities, -c (print in how many cities companies are located)\n");
}

int main(int argc, char* argv[]) {
    char* end;
    if (argc < 2) {
        printf("Must provide a command for the app.\n");
        usage();
        return 1;
    }
    if (strcasecmp(argv[1], "create") == 0) {
        if (argc != 5) {
            printf("At least 3 arguments required for create command. Passed: %d\n", argc - 2);
            return 1;
        }
        if (VALID != input_check_string(argv[2]) || VALID != input_check_string(argv[3])) {
            return 1;
        }
        unsigned int employees = strtoul(argv[4], &end, 10);
        if (*end != '\0') {
            printf("Wrong format of employees count (%s). Provide a positive integer.\n", argv[4]);
            return 1;
        }
    } else if (strcasecmp(argv[1], "update") == 0) {
        if (argc != 6) {
            printf("At least 4 arguments required for update command. Passed: %d\n", argc - 2);
            return 1;
        }
        unsigned int ID = strtoul(argv[2], &end, 10);
        if (*end != '\0') {
            printf("Wrong format of compnay ID (%s). Provide a positive integer.\n", argv[2]);
            return 1;
        }
        if (VALID != input_check_string(argv[3]) || VALID != input_check_string(argv[4])) {
            return 1;
        }
        unsigned int employees = strtoul(argv[5], &end, 10);
        if (*end != '\0') {
            printf("Wrong format of employees count (%s). Provide a positive integer.\n", argv[5]);
            return 1;
        }
    } else if (strcasecmp(argv[1], "delete") == 0) {
        if (argc != 3) {
            printf("Must provide exactly 1 argument for delete command. Passed: %d\n", argc - 2);
            return 1;
        }
        unsigned int ID = strtoul(argv[2], &end, 10);
        if (*end != '\0') {
            printf("Wrong format of company ID (%s). Provide a positive integer.\n", argv[2]);
            return 1;
        }
    } else if (strcasecmp(argv[1], "print") == 0) {
        if (argc != 3) {
            printf("Must provide exactly 1 argument for print command. Passed: %d\n", argc - 2);
            return 1;
        }
        if (strcasecmp(argv[2], "--all") == 0 || strcasecmp(argv[2], "-a") == 0) {
            return 0;
        }
        if (strcasecmp(argv[2], "--employees") == 0 || strcasecmp(argv[2], "-e") == 0) {
            return 0;
        }
        if (strcasecmp(argv[2], "--cities") == 0 || strcasecmp(argv[2], "-c") == 0) {
            return 0;
        }
        unsigned int ID = strtoul(argv[2], &end, 10);
        if (*end != '\0') {
            printf("Wrong format of company ID (%s). Provide a positive integer.\n", argv[2]);
            return 1;
        }
    } else {
        printf("Unknown command: %s\n", argv[1]);
        usage();
        return 1;
    }
}