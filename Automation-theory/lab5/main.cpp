#include <iostream>
#include <string>
#include <vector>
#include <set>
#include <unordered_map>

using namespace std;

bool parse_expression(string&, const unordered_map<char, vector<string>>&);

const unordered_map<char, vector<string>> rhsLists = {
    {'S', {"VQ=MGE", ""}},
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
        if (parse_expression(expression, rhsLists)) {
            cout << "STRING ACCEPTED" << endl;
        } else {
            cout << "STRING REJECTED" << endl;
        }
    }
}

const string getRHS(char terminal, char nonTerminal, bool& emptyAllowed) {
    vector<string> rhsList;
    auto pointer = rhsLists.find(nonTerminal);
    if (pointer != rhsLists.end()) {
        rhsList = pointer->second;
    }
    int n = rhsList.size();
    for (int i = 0; i < n; i++) {
        if (rhsList[i].empty()) {
            emptyAllowed = true;
            continue;
        }
        if (isupper(rhsList[i][0])) {
            string rhs = getRHS(terminal, rhsList[i][0], emptyAllowed);
            if (!rhs.empty()) {
                return rhsList[i];
            }
        }
        if (rhsList[i][0] == terminal) {
            return rhsList[i];
        }
        
    }
    return "";
}

bool validateInput(const string& input,
        const unordered_map<char, vector<string>>& rhsLists) {
    set<char> allowedChars;
    for (auto& l : rhsLists) {
        for (auto& word : l.second) {
            for (auto ch : word) {
                if (!isupper(ch)) {
                    allowedChars.insert(ch);
                }
            }
        }
    }
    allowedChars.insert(' '); allowedChars.insert('\t');
    for (auto ch : input) {
        if (allowedChars.find(ch) == allowedChars.end()) {
            cout << "ERROR: symbol '" << ch << "' is not allowed.\n" <<
                "Allowed symbols are: ";
            for (auto c : allowedChars) {
                cout << "'" << c << "' ";
            }
            cout << '\n';
            return false;
        }
    }
    return true;
}

void getExpected(char ch, set<char>& expected,
        const unordered_map<char, vector<string>>& rhsLists) {
    vector<string> rhsList;
    auto pointer = rhsLists.find(ch);
    if (pointer != rhsLists.end()) {
        rhsList = pointer->second;
    }
    for (auto& word : rhsList) {
        if (word.empty()) continue;
        if (isupper(word[0])) {
            getExpected(word[0], expected, rhsLists);
            continue;
        }
        expected.insert(word[0]);
    }
}

void unexpectedSymbol(char input, string stack,
        const unordered_map<char, vector<string>>& rhsLists) {
    set<char> expected;
    for (char ch : stack) {
        if (isupper(ch)) getExpected(ch, expected, rhsLists);
        else expected.insert(ch);
    }
    if (input == '$') {
        cout << "\nERROR: end of input is unexpected.\n";
    } else {
        cout << "\nERROR: symbol '" << input << "' is unexpected.\n";
    }
    cout  <<"Expected: ";
    for (auto ch : expected) {
        cout << " '" << ch << "' ";
    }
    cout << '\n';
}

bool parse_expression(string& expression,
        const unordered_map<char, vector<string>>& rhsLists) {
    string stack{"S$"};
    if (!validateInput(expression, rhsLists)) {
        return false;
    }
    expression.push_back('$');
    string rhs;
    char curr_expression_char, curr_stack_char;
    string pushedOut;
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
        bool emptyAllowed = false;
        rhs = getRHS(curr_expression_char, curr_stack_char, emptyAllowed);
        if (!rhs.empty()) {
            cout << "'" << curr_stack_char << "' -> '" << rhs << "'\n";
            stack.replace(0, 1, rhs);
            pushedOut.clear();
            continue;
        }
        if (emptyAllowed) {
            cout << "'" << curr_stack_char << "' -> empty string\n";
            stack.erase(0, 1);
            pushedOut.push_back(curr_stack_char);
            continue;
        }
        pushedOut.push_back(curr_stack_char);
        unexpectedSymbol(curr_expression_char, pushedOut, rhsLists);
        break;
    }
    if (expression.empty() && stack.empty()) {
        return true;
    }
    return false;
}