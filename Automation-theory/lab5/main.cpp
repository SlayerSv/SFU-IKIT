#include <iostream>
#include <string>
#include <vector>
#include <set>
#include <unordered_map>

using namespace std;

bool parse_expression(string&, const unordered_map<char, vector<string>>&);
void getValidSymbols(set<char>&);

const unordered_map<char, vector<string>> rhsLists = {
    {'S', {"VQ=MG", ""}},
    {'G', {"NO", "(MNO)O"}},
    {'M', {"-", ""}},
    {'N', {"VQ", "BC"}},
    {'V', {"a", "b", "c", "d"}},
    {'Q', {"V", ""}},
    {'B', {"1", "2", "3", "4", "5", "6", "7", "8", "9"}},
    {'D', {"BH", "0H"}},
    {'C', {".DeZD", "H"}},
    {'H', {"D", ""}},
    {'Z', {"+", "-"}},
    {'O', {"+MG", "*MG", ""}},
    {'$', {}}
};

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(0);
    string expression;
    while (true) {
        cout << "Enter expression for parsing or 'exit': ";
        cout.flush();
        getline(cin, expression);
        if (expression == "exit") break;
        if (parse_expression(expression, rhsLists)) {
            cout << "\nSTRING ACCEPTED" << endl;
        } else {
            cout << "\nSTRING REJECTED" << endl;
        }
    }
}

const string getRHS(char terminal, char nonTerminal, bool& emptyAllowed) {
    auto rhsList = rhsLists.find(nonTerminal)->second;
    int n = rhsList.size();
    for (int i = 0; i < n; i++) {
        if (rhsList[i] == "") {
            emptyAllowed = true;
            continue;
        }
        if (isupper(rhsList[i][0])) {
            string rhs = getRHS(terminal, rhsList[i][0], emptyAllowed);
            if (rhs != "") {
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
    auto rhsList = rhsLists.find(ch)->second;
    for (auto& word : rhsList) {
        if (isupper(word[0])) {
            getExpected(word[0], expected, rhsLists);
            continue;
        }
        expected.insert(word[0]);
    }
}

void unexpectedSymbol(char input, char stack,
        const unordered_map<char, vector<string>>& rhsLists) {
    set<char> expected;
    getExpected(stack, expected, rhsLists);
    cout << "\nERROR: symbol '" << input << "' is unexpected.\n" <<
        "Expected: ";
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
    while (!expression.empty()) {
        cout << "expression: '" << expression << "' stack: '" << stack << "' | ";
        curr_expression_char = expression.front();
        curr_stack_char = stack.front();
        if (curr_expression_char == curr_stack_char) {
            cout << "Pushing out and consuming '" << curr_expression_char << "'\n";
            expression.erase(0, 1);
            stack.erase(0, 1);
            continue;
        }
        bool emptyAllowed = false;
        rhs = getRHS(curr_expression_char, curr_stack_char, emptyAllowed);
        if (rhs != "") {
            cout << "'" << curr_stack_char << "' -> '" << rhs << "'\n";
            stack.replace(0, 1, rhs.data());
            continue;
        }
        if (emptyAllowed) {
            cout << "'" << curr_stack_char << "' -> empty string\n";
            stack.erase(0, 1);
            continue;
        }
        unexpectedSymbol(curr_expression_char, curr_stack_char, rhsLists);
        break;
    }
    if (expression.empty() && stack.empty()) {
        return true;
    }
    return false;
}