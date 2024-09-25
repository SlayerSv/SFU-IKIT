#include "record.h"

int next_record_id = 0;

struct Record* record_new(char* text) {
    struct Record* r = malloc(sizeof(struct Record));
    r->text = malloc((DEFAULT_BUFFER_SIZE) * sizeof(char));
    strcpy(r->text, text);
    r->id = ++next_record_id;
    return r;
}

void record_delete(struct Record* r) {
    free(r->text);
    free(r);
}