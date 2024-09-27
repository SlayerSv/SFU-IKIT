#include "controller.h"

void create_and_add_new_company(struct CLIST* clist) {
    char name[MAX_COMPANY_NAME_SIZE + 1];
    input_take_string(name, MAX_COMPANY_NAME_SIZE + 1, "Enter company's name: ");
    char city[MAX_COMPANY_CITY_SIZE + 1];
    input_take_string(city, MAX_COMPANY_CITY_SIZE + 1, "Enter company's city: ");
    int employees;
    input_take_uint(&employees, "Enter company's number of employees: ");
    struct Company* company = company_new(name, city, employees);
    clist_add(clist, company);
    printf("\nCompany added to the list.\n");
}

void update_company(struct CLIST* clist) {
    if (clist->size == 0) {
        printf("\nList of companies is empty.");
        return;
    }
    int id;
    input_take_uint(&id, "Enter company's ID (0 for cancel): ");
    if (id == 0) return;
    struct Company* company = clist_get(clist, id);
    if (!company) {
        printf("\nCompany wtih ID %d does not exist.\n", id);
        return;
    }
    char name[MAX_COMPANY_NAME_SIZE + 1];
    input_take_string(name, MAX_COMPANY_NAME_SIZE + 1, "Enter new company's name: ");
    char city[MAX_COMPANY_CITY_SIZE + 1];
    input_take_string(city, MAX_COMPANY_CITY_SIZE + 1, "Enter new company's city: ");
    int employees;
    input_take_uint(&employees, "Enter new company's number of employees: ");
    company_update(company, name, city, employees);
    printf("\nCompany has been updated.\n");
}

void delete_company(struct CLIST* clist) {
    if (clist->size == 0) {
        printf("\nList of companies is empty.");
        return;
    }
    int id;
    input_take_uint(&id, "Enter company's ID for deletion (0 for cancel): ");
    if (id == 0) return;
    int deleted = clist_delete(clist, id);
    if (!deleted) {
        printf("\nCompany with ID %d does not exist.\n", id);
        return;
    }
}

void print_company(struct CLIST* clist) {
    if (clist->size == 0) {
        printf("\nList of companies is empty.");
        return;
    }
    int id;
    input_take_uint(&id, "Enter company's ID (0 for cancel): ");
    if (id == 0) return;
    struct Company* company = clist_get(clist, id);
    if (!company) {
        printf("\nCompany with ID %d does not exist.\n", id);
        return;
    }
    company_print(company);
}

void print_all_companies(struct CLIST* clist) {
    if (clist->size == 0) {
        printf("\nList of companies is empty.");
        return;
    }
    int count = 0;
    struct CNODE* tmp = clist->head->next;
    while (tmp) {
        company_print(tmp->company);
        tmp = tmp->next;
        count++;
    }
    printf("\nFound companies: %d\n", count);
}

void print_cities_count(struct CLIST* clist) {
    if (clist->size == 0) {
        printf("\nList of companies is empty.\n");
        return;
    }
    struct CNODE* tmp1 = clist->head->next;
    struct CNODE* tmp2;
    int found;
    int count = 0;
    while (tmp1) {
        found = 0;
        tmp2 = clist->head->next;
        while(tmp2) {
            if (tmp1 == tmp2) break;
            if (strcasecmp(tmp1->company->city, tmp2->company->city) == 0) {
                found = 1;
                break;
            }
            tmp2 = tmp2->next;
        }
        if (!found) count++;
        tmp1 = tmp1->next;
    }
    printf("\nCompanies are in %d different cities.\n", count);
}

void print_most_employed(struct CLIST* clist) {
    if (clist->size == 0) {
        printf("\nList of companies is empty.\n");
        return;
    }
    struct CNODE* tmp = clist->head->next;
    int max = 0;
    while (tmp) {
        if (tmp->company->employees > max) {
            max = tmp->company->employees;
        }
        tmp = tmp->next;
    }
    tmp = clist->head->next;
    printf("\nCompanies with most employees:\n");
    while (tmp) {
        if (tmp->company->employees == max) {
            company_print(tmp->company);
        }
        tmp = tmp->next;
    }
}