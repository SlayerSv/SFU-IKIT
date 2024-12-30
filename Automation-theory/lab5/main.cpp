#include <iostream>
#include <string>
#include <vector>
#include <set>

using namespace std;

const string getRHS(char terminal, char nonTerminal, bool& emptyAllowed);
void validateInput(const string& input);
void unexpectedSymbol(char input, char stack);

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(0);
    string word, stack{"S$"};
    cout << "Enter string for parsing: ";
    cout.flush();
    cin >> word;
    validateInput(word);
    word.push_back('$');
    string rhs;
    char curr_word_char, curr_stack_char;
    while (!word.empty()) {
        cout << "Word: '" << word << "' stack: '" << stack << "' | ";
        curr_word_char = word.front();
        curr_stack_char = stack.front();
        if (curr_word_char == curr_stack_char) {
            cout << "Pushing out and consuming '" << curr_word_char << "'\n";
            word.erase(0, 1);
            stack.erase(0, 1);
            continue;
        }
        bool emptyAllowed = false;
        rhs = getRHS(curr_word_char, curr_stack_char, emptyAllowed);
        if (rhs != "") {
            cout << "'" << curr_stack_char << "' -> '" << rhs << "'\n";
            stack.replace(0, 1, rhs);
            continue;
        }
        if (emptyAllowed) {
            cout << "'" << curr_stack_char << "' -> empty string\n";
            stack.erase(0, 1);
            continue;
        }
        unexpectedSymbol(curr_word_char, curr_stack_char);
        break;
    }
    if (word.empty() && stack.empty()) {
        cout << "STRING ACCEPTED\n";
    } else {
        cout << "STRING REJECTED\n";
    }
}

void print(const string& w) {
    for (auto it = w.rbegin(); it != w.rend(); ++it) {
        cout << *it;
    }
}

const vector<string> S{"VQ=MG", ""};
const vector<string> G{"NO", "(MNO)O"};
const vector<string> M{"-", ""};
const vector<string> N{"VQ", "BC"};
const vector<string> V{"a", "b", "c", "d"};
const vector<string> Q{"V", ""};
const vector<string> B{"1", "2", "3", "4", "5", "6", "7", "8", "9"};
const vector<string> D{"BH", "0H"};
const vector<string> C{".DeZD", "H"};
const vector<string> H{"D", ""};
const vector<string> Z{"+", "-"};
const vector<string> O{"+MG", "*MG", ""};
const vector<string> error{};
const vector<vector<string>> list{S, G, M, N, V, Q, B ,D ,C ,H ,Z ,O};

const vector<string>& getRHSList(char nonTerminal) {
    switch (nonTerminal) {
        case 'S':
            return S;
        case 'G':
            return G;
        case 'M':
            return M;
        case 'N':
            return N;
        case 'V':
            return V;
        case 'Q':
            return Q;
        case 'B':
            return B;
        case 'D':
            return D;
        case 'C':
            return C;
        case 'H':
            return H;
        case 'Z':
            return Z;
        case 'O':
            return O;
        default:
            return error;
    }
}

const string getRHS(char terminal, char nonTerminal, bool& emptyAllowed) {
    auto rhsList = getRHSList(nonTerminal);
    int n = rhsList.size();
    string rhs = "";
    for (int i = 0; i < n; i++) {
        if (rhsList[i] == "") {
            emptyAllowed = true;
            continue;
        }
        if (isupper(rhsList[i][0])) {
            rhs = getRHS(terminal, rhsList[i][0], emptyAllowed);
            if (rhs != "") {
                rhs = rhsList[i];
                break;
            }
        }
        if (rhsList[i][0] == terminal) {
            rhs = rhsList[i];
            break;
        }
        
    }
    return rhs;
}

void validateInput(const string& input) {
    set<char> allowedChars;
    for (auto& l : list) {
        for (auto& word : l) {
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
            exit(1);
        }
    }
}

void getExpected(char ch, set<char>& expected) {
    auto rhsList = getRHSList(ch);
    for (auto& word : rhsList) {
        if (isupper(word[0])) {
            getExpected(word[0], expected);
            continue;
        }
        expected.insert(word[0]);
    }
}

void unexpectedSymbol(char input, char stack) {
    set<char> expected;
    getExpected(stack, expected);
    cout << "\nERROR: symbol '" << input << "' is unexpected.\n" <<
        "Expected: ";
    for (auto ch : expected) {
        cout << " '" << ch << "' ";
    }
    cout << '\n';
}