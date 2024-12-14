.data
.eqv matrixDimension 5
.eqv matrixLen 25
.eqv bufferLen 60
buffer: .space bufferLen
matrix: .space matrixLen
tmpMatrix: .space matrixLen
path: .string "input.txt"
space: .byte 32
carret: .byte 13
newLine: .byte 10
initialMatrix: .string "Initial matrix:\n"
rotate90: .string "\nRotate90:\n"
rotate180: .string "\nRotate180:\n"
rotate270: .string "\nRotate270:\n"
.text
li a7, 1024
la a0, path
li a1, 0
ecall
add s0, a0, zero

li a7, 63
mv a0, s0
la a1, buffer
li a2, bufferLen
ecall

la s0, buffer
la s1, matrix
li t1, 0 #counter
lb t2, carret #carret symbol
li t4, matrixLen
loop:
lb t3, (s0)
addi s0, s0, 2
beq t3, t2, loop
sb t3, (s1)
addi t1, t1, 1
addi s1, s1, 1
bne t1, t4, loop

li a7, 4
la a0, initialMatrix
ecall
call print

call rotate
li a7, 4
la a0, rotate90
ecall
call print

call rotate
li a7, 4
la a0, rotate180
ecall
call print

call rotate
li a7, 4
la a0, rotate270
ecall
call print

exit:
li a7, 10
ecall

print:
la t1, matrix
la t2, buffer
xor t3, t3, t3 #counter
li t4, matrixLen
li t6, matrixDimension
printLoop:
lb t5, (t1)
sb t5, (t2)
addi t2, t2, 1
lb t5, space
sb t5, (t2)
addi t2, t2, 1
addi t1, t1, 1
addi t3, t3, 1
rem t0, t3, t6 #check for end of row
beqz t0, addNewLine
j printLoop

addNewLine:
lb t5, carret
sb t5, (t2)
addi t2, t2, 1
lb t5, newLine
sb t5, (t2)
addi t2, t2, 1
bne t3, t4, printLoop

li a7, 64
li a0, 1
la a1, buffer
li a2, bufferLen
ecall
ret

rotate:
la t1, matrix
la t2, tmpMatrix
xor t3, t3, t3 #row counter
xor t4, t4, t4 #col counter
li t0, matrixDimension
rotateLoop:
sub t6, t0, t3 #set tmpMatrix col counter (dimension - row counter)
addi t6, t6, -1
add t5, t4, zero #set tmpMatrix row counter (same as row counter)
mul t5, t5, t0 #multiple row counter by dimension
add t5, t5, t6 #add col counter
add t6, t2, t5 #set tmp matrix pointer
lb t5, (t1)
sb t5, (t6)
addi t1, t1, 1
addi t4, t4, 1
bne t4, t0, rotateLoop
li t4, 0
addi t3, t3, 1
blt t3, t0, rotateLoop

xor t3, t3, t3
la t1, matrix
li t4, matrixLen
copy:
lb t5, (t2)
sb t5, (t1)
addi t2, t2, 1
addi t1, t1, 1
addi t3, t3, 1
bne t3, t4, copy

ret

