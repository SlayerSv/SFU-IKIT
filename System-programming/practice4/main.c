#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#include "input.h"
#include "companyfile.h"
#include "constants.h"

/// Number of arguments that contain a name of the program and a name of the command.
#define PREFIX_ARGS 2
/// Required number of arguments for the create command.
#define CREATE_ARGS 3
/// Required number of arguments for the update command.
#define UPDATE_ARGS 4
/// Required number of arguments for the delete command.
#define DELETE_ARGS 1
/// Required number of arguments for the print command.
#define PRINT_ARGS 1

void usage() {
    printf("\nUsage:\n\
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
        printf("\nMust provide a command for the app.\n");
        usage();
        return EXIT_FAILURE;
    }
    if (strcasecmp(argv[1], "create") == SAME) {
        if (argc != PREFIX_ARGS + CREATE_ARGS) {
            printf("At least %d arguments required for create command. Passed: %d\n",
                CREATE_ARGS, argc - PREFIX_ARGS);
            return EXIT_FAILURE;
        }
        if (VALID != input_check_string(argv[2]) || VALID != input_check_string(argv[3])) {
            return EXIT_FAILURE;
        }
        int employees = strtol(argv[4], &end, 10);
        if (*end != '\0' || employees < 0) {
            printf("Wrong format of employees count (%s). Provide a non-negative integer.\n",
                argv[4]);
            return EXIT_FAILURE;
        }
        file_add_company(argv[2], argv[3], argv[4]);
    } else if (strcasecmp(argv[1], "update") == SAME) {
        if (argc != PREFIX_ARGS + UPDATE_ARGS) {
            printf("At least %d arguments required for update command. Passed: %d\n",
                UPDATE_ARGS, argc - PREFIX_ARGS);
            return EXIT_FAILURE;
        }
        int id = strtol(argv[2], &end, 10);
        if (*end != '\0' || id < 1) {
            printf("Wrong format of compnay ID (%s). Provide a positive integer.\n", argv[2]);
            return EXIT_FAILURE;
        }
        if (VALID != input_check_string(argv[3]) || VALID != input_check_string(argv[4])) {
            return EXIT_FAILURE;
        }
        int employees = strtol(argv[5], &end, 10);
        if (*end != '\0') {
            printf("Wrong format of employees count (%s). Provide a non-negative integer.\n",
                argv[4]);
            return EXIT_FAILURE;
        }
        file_update_company(id, argv[3], argv[4], argv[5]);
    } else if (strcasecmp(argv[1], "delete") == SAME) {
        if (argc != PREFIX_ARGS + DELETE_ARGS) {
            printf("Must provide exactly %d arguments for delete command. Passed: %d\n",
                DELETE_ARGS, argc - PREFIX_ARGS);
            return EXIT_FAILURE;
        }
        int id = strtol(argv[2], &end, 10);
        if (*end != '\0' || id < 1) {
            printf("Wrong format of company ID (%s). Provide a positive integer.\n", argv[2]);
            return EXIT_FAILURE;
        }
        file_delete_company(id);
    } else if (strcasecmp(argv[1], "print") == SAME) {
        if (argc != PREFIX_ARGS + PRINT_ARGS) {
            printf("Must provide exactly %d arguments for print command. Passed: %d\n",
                PRINT_ARGS, argc - PREFIX_ARGS);
            return EXIT_FAILURE;
        }
        if (strcasecmp(argv[2], "all") == SAME) {
            file_print_all();
            return EXIT_SUCCESS;
        }
        if (strcasecmp(argv[2], "employees") == SAME) {
            file_print_most_employed();
            return EXIT_SUCCESS;
        }
        if (strcasecmp(argv[2], "cities") == SAME) {
            file_print_cities_count();
            return EXIT_SUCCESS;
        }
        int id = strtol(argv[2], &end, 10);
        if (*end != '\0' || id < 1) {
            printf("Wrong format of company ID (%s). Provide a positive integer.\n", argv[2]);
            return EXIT_FAILURE;
        }
        file_print_company(id);
    } else {
        printf("Unknown command: %s\n", argv[1]);
        usage();
        return EXIT_FAILURE;
    }
}
