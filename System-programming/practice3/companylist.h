#ifndef COMPANY_LIST
#define COMPANY_LIST

#include <stdlib.h>
#include "company.h"

/// Single linked list of companies.
struct CLIST {
    struct CNODE* head;
    int size;
};
/// Node of a single linked list. 
struct CNODE {
    struct Company* company;
    struct CNODE* next;
};

struct CLIST* clist_new();
void clist_add(struct CLIST* cl, struct Company* company);
int clist_delete(struct CLIST* cl, int id);
struct Company* clist_get(struct CLIST* cl, int id);
void clist_clear(struct CLIST* clist);

#endif