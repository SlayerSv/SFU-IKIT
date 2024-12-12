.data
a: .dword 0
x: .dword 0
promptX: .string "Enter x: "
promptA: .string "Enter a: "
# y = y1 % y2
# y1 = 8 + |x| if x < 1, else |a| * 2
# y2 = 3, if x = a, else a + 1
# loop x + 0-9
.text
#print prompt for A value
li a7, 4
la a0, promptA
ecall

#read entered int
li a7, 5
ecall
#store entered int in a var
sw a0, a, t1

#print prompt for X value
li a7, 4
la a0, promptX
ecall

#read entered int
li a7, 5
ecall
#store entered int in a var
sw a0, x, t1

li a4, 10
loop: # inc X 10 times and calc y = y1 % y2
beq a3, a4, exit
call calc

mv a0, s3
li a7, 1
ecall

call printNewLine

lw t1, x
addi t1, t1, 1
sw t1, x, t3

addi a3, a3, 1
j loop

calc: #y1 = 8 + |x| if x < 1, else |a| * 2
lw t1, x
li t2, 1
blt t1, t2, eightPlusX
lw t1, a
bgez t1, aMul2
neg t1, t1

aMul2:
li t2, 2
mul t1, t1, t2
add s1, t1, zero
j y2
eightPlusX:
neg t1, t1
addi t1, t1, 8
add s1, t1, zero

y2: # y2 = 3, if x = a, else a + 1
lw t1, x
lw t2, a
beq t1, t2, y2equals3
addi s2, t2, 1
j y
y2equals3:
li s2, 3

y: # y = y1 % y2
rem s3, s1, s2
ret

printNewLine:
li a7, 11
li a0, 10
ecall
ret

exit:
li a7, 10
ecall
