section	.data
    dimension db 4
    matrix db 1, 2, 3, 4, 5, 6, 7, 8, 9, 9, 8, 7, 6, 5, 4, 3
    matrixSize equ $ - matrix
    row db 0
    col db 0
    outputSize db 0
    msg1 db 'Initial matrix:', 10
    msg1Len equ $ - msg1
    msg2 db 'Modified matrix:', 10
    msg2Len equ $ - msg2
    errorMsg db 'ERROR: value must be an integer',10
    lenError equ $ - errorMsg
    sign db 1

section .bss
    output resb 50
    input resb 50

section	.text
	global _start

_start:
	mov eax, 4
	mov ebx, 1
	mov ecx, msg1
	mov edx, msg1Len
	int 0x80

	call itoaMatrix
    
    mov eax, 4
    mov ebx, 1
    mov ecx, output
    movzx edx, byte [outputSize]
    int 0x80
    
    call iterateMatrix
    
    mov eax, 4
	mov ebx, 1
	mov ecx, msg2
	mov edx, msg2Len
	int 0x80
    
    call itoaMatrix

    mov eax, 4
    mov ebx, 1
    mov ecx, output
    movzx edx, byte [outputSize]
    int 0x80
    
	mov ebx, 0
	call exit

iterateMatrix:
    push eax
    push ebx
    push ecx
    push edx
    xor ecx, ecx
    xor edx, edx
    movzx ebx, byte [dimension]
    
iterateMatrixLoop:
    cmp ecx, ebx
    je return
    call checkMainDiagonal
    inc edx
    cmp edx, ebx
    je nextRow
    jmp iterateMatrixLoop
    
nextRow:
    inc ecx
    mov edx, 0
    jmp iterateMatrixLoop

checkMainDiagonal:
    cmp edx, ecx
    jge checkSecondaryDiagonal
    ret
    
checkSecondaryDiagonal:
    push ebx
    sub ebx, ecx
    cmp edx, ebx
    pop ebx
    jl zeroCell
    ret

zeroCell:
    push eax
    push ebx
    push ecx
    push edx
    mov eax, ecx
    mul ebx
    pop edx
    add eax, edx
    push edx
    mov [matrix + eax], byte 0
    jmp return

itoaMatrix:
    push eax
    push ebx
    push ecx
    push edx
    xor ecx, ecx
    xor ebx, ebx
    xor edx, edx

itoaMatrixLoop:
    cmp ecx, matrixSize
    je return
    mov bl, byte [matrix + ecx]
    add bl, 48
    mov [output + edx], bl
    inc edx
    mov [output + edx], byte 32
    inc ecx
    inc edx
    call checkEndOfRow
    jmp itoaMatrixLoop

checkEndOfRow:
    push eax
    push ebx
    push ecx
    push edx
    xor edx, edx
    mov eax, ecx
    movzx ebx, byte [dimension]
    div ebx
    cmp edx, 0
    jne return

pushNewLine:
    pop edx
    mov [output + edx], byte 10
    inc edx
    push edx
    mov [outputSize], dl
    jmp return

return:
    pop edx
    pop ecx
    pop ebx
    pop eax
    ret

exit:
	mov	eax, 1
	int	0x80
