#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#include "input.h"
#include "companyfile.h"

void usage() {
    printf("Usage:\n\
create [company name] [company city] [employee count] (create new company)\n\
update [company ID] [company name] [company city] [employee count] (update company)\n\
delete [company ID] (delete company)\n\
print [company ID] (print company)\n\
print all (print all companies)\n\
print employees (print companies with most employees)\n\
print cities (print in how many cities companies are located)\n");
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
        strtoul(argv[4], &end, 10);
        if (*end != '\0') {
            printf("Wrong format of employees count (%s). Provide a positive integer.\n", argv[4]);
            return 1;
        }
        file_add_company(argv[2], argv[3], argv[4]);
    } else if (strcasecmp(argv[1], "update") == 0) {
        if (argc != 6) {
            printf("At least 4 arguments required for update command. Passed: %d\n", argc - 2);
            return 1;
        }
        unsigned int id = strtoul(argv[2], &end, 10);
        if (*end != '\0' || id < 1) {
            printf("Wrong format of compnay ID (%s). Provide a positive integer.\n", argv[2]);
            return 1;
        }
        if (VALID != input_check_string(argv[3]) || VALID != input_check_string(argv[4])) {
            return 1;
        }
        strtoul(argv[5], &end, 10);
        if (*end != '\0') {
            printf("Wrong format of employees count (%s). Provide a positive integer.\n", argv[5]);
            return 1;
        }
        file_update_company(id, argv[3], argv[4], argv[5]);
    } else if (strcasecmp(argv[1], "delete") == 0) {
        if (argc != 3) {
            printf("Must provide exactly 1 argument for delete command. Passed: %d\n", argc - 2);
            return 1;
        }
        unsigned int id = strtoul(argv[2], &end, 10);
        if (*end != '\0' || id < 1) {
            printf("Wrong format of company ID (%s). Provide a positive integer.\n", argv[2]);
            return 1;
        }
        file_delete_company(id);
    } else if (strcasecmp(argv[1], "print") == 0) {
        if (argc != 3) {
            printf("Must provide exactly 1 argument for print command. Passed: %d\n", argc - 2);
            return 1;
        }
        if (strcasecmp(argv[2], "all") == 0) {
            file_print_all();
            return 0;
        }
        if (strcasecmp(argv[2], "employees") == 0) {
            file_print_most_employed();
            return 0;
        }
        if (strcasecmp(argv[2], "cities") == 0) {
            file_print_cities_count();
            return 0;
        }
        unsigned int id = strtoul(argv[2], &end, 10);
        if (*end != '\0' || id < 1) {
            printf("Wrong format of company ID (%s). Provide a positive integer.\n", argv[2]);
            return 1;
        }
        file_print_company(id);
    } else {
        printf("Unknown command: %s\n", argv[1]);
        usage();
        return 1;
    }
}