#include "controller.h"

#define EXIT 0
#define PRINT_ALL 1
#define ADD 2
#define UPDATE 3
#define DELETE 4
#define PRINT 5
#define MOST_EMPLOYED 6
#define CITIES_COUNT 7

int main() {
    printf("Welcome to companies manager app!\n");
    char input[DEFAULT_BUFFER_SIZE];
    int option;
    struct CLIST* clist = clist_new();
    while (1) {
        printf("\n1 Print all companies\n2 Create new company\n3 Update company\n\
4 Delete company\n5 Print a company\n6 Companies with most employees\n\
7 Count number of cities\n\n0 Exit\n\n");
        input_take_uint(&option, "Choose an option: ");
        switch (option) {
            case EXIT:
                clist_clear(clist);
                printf("Exiting program.");
                return 0;
            case PRINT_ALL:
                print_all_companies(clist);
                break;
            case ADD:
                create_and_add_new_company(clist);
                break;
            case UPDATE:
                update_company(clist);
                break;
            case DELETE:
                delete_company(clist);
                break;
            case PRINT:
                print_company(clist);
                break;
            case MOST_EMPLOYED:
                print_most_employed(clist);
                break;
            case CITIES_COUNT:
                print_cities_count(clist);
                break;
            default:
                printf("Unknown option.\n");
                break;
        }
    }
}