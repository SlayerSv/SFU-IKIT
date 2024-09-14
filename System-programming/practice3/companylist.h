#ifndef COMPANY_LIST
#define COMPANY_LIST

struct CLIST {
    struct CNODE* head;
};

struct CNODE {
    struct Company* company;
    struct Company* next;
};

struct CLIST* clist_new();
void clist_add(struct CLIST* cl);
void clist_delete(struct CLIST* cl);
void clist_most_employed(struct CLIST* cl);
void clist_print_cities_count(struct CLIST* cl);
void clist_print_all(struct CLIST* cl);
void clist_print(struct CLIST* cl);
void clist_update(struct CLIST* cl);

#endif