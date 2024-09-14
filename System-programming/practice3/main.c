#include <stdio.h>
#include <string.h>

#include "input.h"
#include "company.h"
#include "companylist.h"

int main() {
    printf("Welcome to companies manager app!\n");
    char input[MAX_STRING_SIZE+1];
    char message[] = "Choose an option: ";
    unsigned int option;
    struct CLIST* clist = clist_new();
    while (1) {
        print_menu();
        input_take_uint(&option, message);
        switch (option) {
            case 0:
                printf("Exiting program.");
                return 0;
            case 1:
                clist_print_all(clist);
            case 2:
                clist_add(clist);
            case 3:
                clist_update(clist);
            case 4:
                clist_delete(clist);
            case 5:
                clist_print(clist);
            case 6:
                clist_most_employed(clist);
            case 7:
                clist_print_cities_count(clist);
            default:
                printf("Unknown option.\n");
        }

    }
}

void print_menu() {
    printf("1 Print all companies\n2 Create new company\n3 Update company\n \
    4 Delete company\n5 Print a company\n6 Companies with most employees\n \
    7 Count number of cities\n\n0 Exit\n");
}