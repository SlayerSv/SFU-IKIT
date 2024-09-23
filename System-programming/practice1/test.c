#include <CUnit/Basic.h>
#include "process_values.h"
#include "constants.h"

void test_double(void) {
    char output[DEFAULT_BUFF_SIZE];
    char input1[] = "0.1";
    char input2[] = "0.2";
    double expected = 0.3;
    processValues(output, input1, input2);
    double ans = strtod(output, NULL);
    CU_ASSERT_EQUAL(ans, expected);

    char input3[] = "-.2";
    char input4[] = "+.1";
    expected = -0.1;
    processValues(output, input3, input4);
    ans = strtod(output, NULL);
    CU_ASSERT_EQUAL(ans, expected);

    char input5[] = "3e2";
    char input6[] = "-5E-5";
    expected = 299.99995;
    processValues(output, input5, input6);
    ans = strtod(output, NULL);
    CU_ASSERT_EQUAL(ans, expected);
}

void test_integers(void) {
    char output[DEFAULT_BUFF_SIZE];
    char input1[] = "1";
    char input2[] = "2";
    long long expected = 1;
    processValues(output, input1, input2);
    long long ans = strtoll(output, NULL, 10);
    CU_ASSERT_EQUAL(ans, expected);

    char input3[] = "-5";
    char input4[] = "+7";
    expected = -5;
    processValues(output, input3, input4);
    ans = strtoll(output, NULL, 10);
    CU_ASSERT_EQUAL(ans, expected);

    char input5[] = "99999999999999999";
    char input6[] = "100000000000000000";
    expected = 99999999999999999;
    processValues(output, input5, input6);
    ans = strtoll(output, NULL, 10);
    CU_ASSERT_EQUAL(ans, expected);
}

void test_strings(void) {
    char output[DEFAULT_BUFF_SIZE];
    char input1[] = "--1";
    char input2[] = "++2";
    char expected1[] ="--1++2";
    processValues(output, input1, input2);
    CU_ASSERT_STRING_EQUAL(output, expected1);

    char input3[] = "SFU ";
    char input4[] = "IKIT";
    char expected2[] = "SFU IKIT";
    processValues(output, input3, input4);
    CU_ASSERT_STRING_EQUAL(output, expected2);

    char input5[] = "";
    char input6[] = "";
    char expected3[] = "";
    processValues(output, input5, input6);
    CU_ASSERT_STRING_EQUAL(output, expected3);
}

int main() {
    CU_pSuite suite;
    CU_initialize_registry();
    suite = CU_add_suite("main_suite", NULL, NULL);
    CU_ADD_TEST(suite, test_double);
    CU_ADD_TEST(suite, test_integers);
    CU_ADD_TEST(suite, test_strings);
    CU_basic_run_tests();

    CU_cleanup_registry();
    return CU_get_error();
}