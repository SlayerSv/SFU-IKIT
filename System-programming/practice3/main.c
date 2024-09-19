#include <stdio.h>
#include <string.h>

#include "input.h"
#include "company.h"
#include "companylist.h"

int main() {
    printf("Welcome to companies manager app!\n");
    char input[1000];
    int option;
    struct CLIST* clist = clist_new();
    while (1) {
        printf("\n1 Print all companies\n2 Create new company\n3 Update company\n\
4 Delete company\n5 Print a company\n6 Companies with most employees\n\
7 Count number of cities\n\n0 Exit\n\n");
        input_take_uint(&option, "Choose an option: ");
        switch (option) {
            case 0:
                printf("Exiting program.");
                return 0;
            case 1:
                clist_print_all(clist);
                break;
            case 2:
                clist_add(clist);
                break;
            case 3:
                clist_update(clist);
                break;
            case 4:
                clist_delete(clist);
                break;
            case 5:
                clist_print(clist);
                break;
            case 6:
                clist_most_employed(clist);
                break;
            case 7:
                clist_print_cities_count(clist);
                break;
            default:
                printf("Unknown option.\n");
                break;
        }

    }
}