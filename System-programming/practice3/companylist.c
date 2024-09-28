#include "companylist.h"

/// @brief Create and initialize a new single linked list of companies.
/// @returns Single linked list of companies ready to use.
struct CLIST* clist_new() {
    struct CLIST* clist = malloc(sizeof(struct CLIST));
    clist->head = malloc(sizeof(struct CNODE));
    clist->head->next = NULL;
    clist->head->company = NULL;
    clist->size = 0;
    return clist;
}

/// @brief Add a new company to a list of companies.
/// @param cl SLL of companies to which the new company will be added.
/// @param company A new company that will be added to the list.
void clist_add(struct CLIST* clist, struct Company* company) {
    struct CNODE* cnode = malloc(sizeof(struct CNODE));
    cnode->company = company;
    cnode->next = clist->head->next;
    clist->head->next = cnode;
    clist->size++;
}

/// @brief Finds and returns a company with specified id.
/// @param clist List of companies to search.
/// @param id Id of the company to find.
/// @return Pointer to company struct if company is present in the list.
///         NULL if company is not in the list.
struct Company* clist_get(struct CLIST* clist, int id) {
    struct CNODE* tmp = clist->head->next;
    while (tmp) {
        if (tmp->company->id == id)
            return tmp->company;
        tmp = tmp->next;
    }
    return NULL;
}

/// @brief Deletes a company from a list.
/// @param clist List of companies.
/// @param id ID of a company to delete.
/// @return Number of companies deleted.
int clist_delete(struct CLIST* clist, int id) {
    if (clist->size == 0) {
        return 0;
    }
    struct CNODE* prev = clist->head;
    struct CNODE* tmp = clist->head->next;
    while (tmp) {
        if (tmp->company->id == id) {
            prev->next = tmp->next;
            company_delete(tmp->company);
            free(tmp);
            clist->size--;
            return 1;
        }
        prev = tmp;
        tmp = tmp->next;
    }
    return 0;
}

/// @brief Clear a list deleting all companies and the list itself.
/// @param clist List of the companies to clear.
void clist_clear(struct CLIST* clist) {
    struct CNODE* curr = clist->head;
    struct CNODE* next;
    while (curr) {
        next = curr->next;
        company_delete(curr->company);
        free(curr);
        curr = next;
    }
    free(clist);
}