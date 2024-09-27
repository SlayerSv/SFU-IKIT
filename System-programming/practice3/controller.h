#ifndef CONTROLLER
#define CONTROLLER

#include <string.h>

#include "company.h"
#include "companylist.h"
#include "constants.h"
#include "input.h"

void create_and_add_new_company(struct CLIST* clist);
void update_company(struct CLIST* clist);
void delete_company(struct CLIST* clist);
void print_company(struct CLIST* clist);
void print_all_companies(struct CLIST* cl);
void print_cities_count(struct CLIST* clist);
void print_most_employed(struct CLIST* clist);

#endif