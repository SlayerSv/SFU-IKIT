#include <company.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "companylist.h"
#include "input.h"

struct CLIST* clist_new() {
    struct CLIST* clist = malloc(sizeof(struct CLIST));
    clist->head = malloc(sizeof(struct CNODE));
    clist->head->next = NULL;
    clist->head->company = NULL;
    return clist;
}

void clist_add(struct CLIST* cl) {
    char name[MAX_STRING_SIZE + 1];
    input_take_string(name, "Enter company's name: ");
    char city[MAX_STRING_SIZE + 1];
    input_take_string(city, "Enter company's city: ");
    unsigned int employees;
    input_take_uint(&employees, "Enter company's number of employees: ");
    struct Company* c = company_new(name, city, employees);
    struct CNODE* cnode = malloc(sizeof(struct CNODE));
    cnode->company = c;
    cnode->next = cl->head->next;
    cl->head->next = cnode;
    printf("Company added to the list.\n");
}

void clist_update(struct CLIST* cl) {
    unsigned int id;
    input_take_uint(&id, "Enter company's ID (0 for cancel): ");
    if (id == 0) return;
    struct Company* c = clist_get(cl, id);
    if (!c) {
        printf("Company wtih ID %d does not exist.\n", id);
        return;
    }
    char name[MAX_STRING_SIZE + 1];
    input_take_string(name, "Enter new company's name: ");
    char city[MAX_STRING_SIZE + 1];
    input_take_string(city, "Enter new company's city: ");
    unsigned int employees;
    input_take_uint(&employees, "Enter new company's number of employees: ");
    strcpy(c->name, name);
    strcpy(c->city, city);
    c->employees = employees;
    printf("Company has been updated.\n");
}

struct Company* clist_get(struct CLIST* cl, unsigned int id) {
    struct CNODE* tmp = cl->head->next;
    while (tmp) {
        if (tmp->company->id == id) return tmp->company;
        tmp = tmp->next;
    }
    return NULL;
}

void clist_delete(struct CLIST* cl) {
    unsigned int id;
    input_take_uint(&id, "Enter company's ID for deletion (0 for cancel): ");
    if (id == 0) return;
    struct CNODE* prev = cl->head;
    struct CNODE* tmp = cl->head->next;
    int found = 0;
    while (tmp) {
        if (tmp->company->id == id) {
            prev->next = tmp->next;
            company_delete(tmp->company);
            free(tmp);
            found = 1;
            printf("Company with ID %d has been deleted.\n", id);
            break;
        }
        prev = tmp;
        tmp = tmp->next;
    }
    if (!found) {
        printf("Company with ID %d does not exist.\n", id);
    }
}

void clist_most_employed(struct CLIST* cl) {
    struct CNODE* tmp = cl->head->next;
    struct CNODE* c = tmp;
    int max = 0;
    while (tmp) {
        if (tmp->company->employees > max) {
            max = tmp->company->employees;
        }
        tmp = tmp->next;
    }
    tmp = cl->head->next;
    printf("Companies with most employees:\n");
    while (tmp) {
        if (tmp->company->employees == max) {
            company_print(tmp->company);
        }
        tmp = tmp->next;
    }
}

void clist_print_cities_count(struct CLIST* cl) {
    struct CNODE* tmp1 = cl->head->next;
    struct CNODE* tmp2;
    int found;
    int count = 0;
    while (tmp1) {
        found = 0;
        tmp2 = cl->head->next;
        while(tmp2) {
            if (tmp1 != tmp2 && strcasecmp(tmp1->company->city, tmp2->company->city) == 0) {
                found = 1;
                break;
            }
            tmp2 = tmp2->next;
        }
        if (!found) count++;
        tmp1 = tmp1->next;
    }
    printf("Companies are in %d different cities.\n");
}

void clist_print_all(struct CLIST* cl) {
    int count = 0;
    struct CNODE* tmp = cl->head->next;
    while (tmp) {
        company_print(tmp->company);
        tmp = tmp->next;
        count++;
    }
    printf("Found companies: %d\n", count);
}

void clist_print(struct CLIST* cl) {
    unsigned int id;
    input_take_uint(&id, "Enter company's ID (0 for cancel): ");
    if (id == 0) return;
    struct CNODE* tmp = cl->head->next;
    int found = 0;
    while (tmp) {
        if (tmp->company->id == id) {
            company_print(tmp->company);
            found = 1;
            break;
        }
        tmp = tmp->next;
    }
    if (!found) {
        printf("Company with ID %d does not exist.\n", id);
    }
}