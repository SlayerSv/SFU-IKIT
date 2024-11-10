CC = gcc

dfa: dfa.out
	./dfa.out

nfa: nfa.out
	./nfa.out

dfa.out: dfa.o
	$(CC) -o $@ $^

nfa.out: nfa.o state.o
	$(CC) -o $@ $^