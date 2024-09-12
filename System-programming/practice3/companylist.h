#ifndef COMPANY_LIST
#define COMPANY_LIST

struct CLIST {
    struct CNODE* head;
};

struct CNODE {
    struct Company* company;
    struct Company* next;
};

struct CLIST* clist_init();
void clist_add(struct CLIST* cl, struct Company* c);
int clist_delete(struct CLIST* cl, int id);
void clist_most_employed(struct CLIST* cl);
void clist_print_cities_count(struct CLIST* cl);
void clist_print_all(struct CLIST* cl);
int clist_print(struct CLIST* cl, int id);

#endif