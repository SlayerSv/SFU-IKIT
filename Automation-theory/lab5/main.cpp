#include <iostream>
#include <string>
#include <vector>
#include <set>
#include <unordered_map>

using namespace std;

bool parse_expression(string&, const unordered_map<char, vector<string>>&);

const unordered_map<char, vector<string>> rhs_lists = {
    {'S', {"VQ=MGE;S", ""}},
    {'G', {"(MGOMGE)", "N"}},
    {'E', {"OMGE", ""}},
    {'M', {"-", ""}},
    {'N', {"VQ", "CF"}},
    {'V', {"a", "b", "c", "d"}},
    {'Q', {"V", ""}},
    {'C', {"1", "2", "3", "4", "5", "6", "7", "8"}},
    {'D', {"CH", "0H"}},
    {'F', {".DeZD", "H"}},
    {'H', {"D", ""}},
    {'Z', {"+", "-"}},
    {'O', {"+", "*"}}
};

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(0);
    string expression;
    while (true) {
        cout << "\nEnter expression for parsing or 'exit': ";
        cout.flush();
        getline(cin, expression);
        if (expression == "exit") break;
        if (parse_expression(expression, rhs_lists)) {
            cout << "STRING ACCEPTED" << endl;
        } else {
            cout << "STRING REJECTED" << endl;
        }
    }
}

const string getRHS(char terminal, char non_terminal, bool& empty_allowed) {
    vector<string> rhs_list;
    auto pointer = rhs_lists.find(non_terminal);
    if (pointer != rhs_lists.end()) {
        rhs_list = pointer->second;
    }
    int n = rhs_list.size();
    for (int i = 0; i < n; i++) {
        if (rhs_list[i].empty()) {
            empty_allowed = true;
            continue;
        }
        if (isupper(rhs_list[i][0])) {
            string rhs = getRHS(terminal, rhs_list[i][0], empty_allowed);
            if (!rhs.empty()) {
                return rhs_list[i];
            }
        }
        if (rhs_list[i][0] == terminal) {
            return rhs_list[i];
        }
        
    }
    return "";
}

bool validate_input(const string& input,
        const unordered_map<char, vector<string>>& rhs_lists) {
    set<char> allowed_chars;
    for (auto& rhs_list : rhs_lists) {
        for (const string& rhs : rhs_list.second) {
            for (char ch : rhs) {
                if (!isupper(ch)) {
                    allowed_chars.insert(ch);
                }
            }
        }
    }
    allowed_chars.insert(' '); allowed_chars.insert('\t');
    for (char ch : input) {
        if (allowed_chars.find(ch) == allowed_chars.end()) {
            cout << "ERROR: symbol '" << ch << "' is not allowed.\n" <<
                "Allowed symbols are: ";
            for (char ch : allowed_chars) {
                cout << "'" << ch << "' ";
            }
            cout << '\n';
            return false;
        }
    }
    return true;
}

void get_expected(char ch, set<char>& expected,
        const unordered_map<char, vector<string>>& rhs_lists) {
    vector<string> rhs_list;
    auto pointer = rhs_lists.find(ch);
    if (pointer != rhs_lists.end()) {
        rhs_list = pointer->second;
    }
    for (const string& rhs : rhs_list) {
        if (rhs.empty()) continue;
        if (isupper(rhs[0])) {
            get_expected(rhs[0], expected, rhs_lists);
            continue;
        }
        expected.insert(rhs[0]);
    }
}

void unexpected_symbol(char input, const string& stack,
        const unordered_map<char, vector<string>>& rhs_lists) {
    set<char> expected;
    for (char ch : stack) {
        if (isupper(ch)) get_expected(ch, expected, rhs_lists);
        else expected.insert(ch);
    }
    if (input == '$') {
        cout << "\nERROR: end of input is unexpected.\n";
    } else {
        cout << "\nERROR: symbol '" << input << "' is unexpected.\n";
    }
    cout  <<"Expected: ";
    for (char ch : expected) {
        cout << " '" << ch << "' ";
    }
    cout << '\n';
}

bool parse_expression(string& expression,
        const unordered_map<char, vector<string>>& rhs_lists) {
    string stack{"S$"};
    if (!validate_input(expression, rhs_lists)) {
        return false;
    }
    expression.push_back('$');
    string rhs;
    char curr_expression_char, curr_stack_char;
    string pushed_out;
    while (!expression.empty()) {
        curr_expression_char = expression.front();
        if (curr_expression_char == ' ' || curr_expression_char == '\t') {
            expression.erase(0, 1);
            continue;
        }
        cout << "expression: '" << expression << "' stack: '" << stack << "' | ";
        curr_stack_char = stack.front();
        if (curr_expression_char == curr_stack_char) {
            cout << "Pushing out and consuming '" << curr_expression_char << "'\n";
            expression.erase(0, 1);
            stack.erase(0, 1);
            continue;
        }
        bool empty_allowed = false;
        rhs = getRHS(curr_expression_char, curr_stack_char, empty_allowed);
        if (!rhs.empty()) {
            cout << "'" << curr_stack_char << "' -> '" << rhs << "'\n";
            stack.replace(0, 1, rhs);
            pushed_out.clear();
            continue;
        }
        if (empty_allowed) {
            cout << "'" << curr_stack_char << "' -> empty string\n";
            stack.erase(0, 1);
            pushed_out.push_back(curr_stack_char);
            continue;
        }
        pushed_out.push_back(curr_stack_char);
        unexpected_symbol(curr_expression_char, pushed_out, rhs_lists);
        break;
    }
    if (expression.empty() && stack.empty()) {
        return true;
    }
    return false;
}