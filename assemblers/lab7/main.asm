mov r0, #prompt
str r0, .WriteString
mov r1, #input
str r1, .ReadString
mov r6, #32 ;previous character, initial value is space

loop:
ldrb r5, [r1] ;load current character
bl incrementCounters
bl toUpperCase
add r1, r1, #1 ;move input string pointer
mov r6, r5 ;set prev character to current character
b loop

incrementCounters:
cmp r5, #0
beq end
cmp r5, #32
beq return
add r11, r11, #1 ;increase characters counter
cmp r6, #32
bne return
add r12, r12, #1 ;increase words counter
b return

toUpperCase:
cmp r5, #97 ;char 'a'
blt return
cmp r5, #122 ;char 'z'
bgt return
sub r7, r5, #32 ;to upper case
strb r7, [r1]
b return 

end:
bl divide
mov r1, #characters
str r1, .WriteString
str r10, .WriteUnsignedNum
mov r1, #words
str r1, .WriteString
str r12, .WriteUnsignedNum
mov r1, #10
str r1, .WriteChar
mov r1, #input
str r1, .WriteString
hlt

divide:
cmp r11, r12
blt return
sub r11, r11, r12
add r10, r10, #1
b divide

return:
ret

.data
prompt: .asciz "Enter a sentence: "
characters: .asciz "\nAverage number of characters in words: "
words: .asciz "\nNumber of words: "
input: .block 127