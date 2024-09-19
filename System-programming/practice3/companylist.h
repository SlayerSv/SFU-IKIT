#ifndef COMPANY_LIST
#define COMPANY_LIST

struct CLIST {
    struct CNODE* head;
};

struct CNODE {
    struct Company* company;
    struct CNODE* next;
};

struct CLIST* clist_new();
void clist_add(struct CLIST* cl);
void clist_delete(struct CLIST* cl);
void clist_most_employed(struct CLIST* cl);
void clist_print_cities_count(struct CLIST* cl);
void clist_print_all(struct CLIST* cl);
void clist_print(struct CLIST* cl);
void clist_update(struct CLIST* cl);
struct Company* clist_get(struct CLIST* cl, int id);

#endif