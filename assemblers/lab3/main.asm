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
    
    call zeroMainDiagonal
    
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

zeroMainDiagonal:
    push eax
    push ebx
    push ecx
    push edx
    xor ecx, ecx
    xor edx, edx
    movzx ebx, byte [dimension]
    
zeroMainDiagonalLoop:
    cmp ecx, ebx
    je return
    call zeroCell
    inc edx
    cmp edx, ebx
    je nextRow
    jmp zeroMainDiagonalLoop
    
nextRow:
    inc ecx
    mov edx, ecx
    jmp zeroMainDiagonalLoop
    
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

atoi:
    mov esi, input
	xor eax, eax
	xor ecx, ecx
	call setSign

atoiLoop:
    mov cl, byte [esi]
    cmp ecx, 10
    je negate
    cmp ecx, 48
    jl error
    cmp ecx, 57
    jg error
    mov ebx, 10
    mul ebx
    sub ecx, 48
    add eax, ecx
    inc esi
    jmp atoiLoop

setSign:
    mov [sign], byte 1
    cmp [esi], byte 45
    je setMinus
    ret

setMinus:
    inc esi
    mov [sign], byte -1
    ret

negate:
    cmp [sign], byte 0
    jg return
    xor eax, -1
    inc eax
    ret

return:
    pop edx
    pop ecx
    pop ebx
    pop eax
    ret

convertAndPrint:
    push eax
    push ebx
    push ecx
    push edx
    cmp eax, 0
    je printZero
    push 0
    xor ecx, ecx
    call checkSign

itoa:
    cmp eax, 0
    je addSign
    xor edx, edx
    mov ebx, 10
    div ebx
	add edx, 48
	push edx
	jmp itoa

addSign:
    cmp [sign], byte 0
    jl addMinus
    jmp reverse

addMinus:
    push 45
	jmp reverse

reverse:
    pop ebx
    cmp bl, 0
    je print
    mov [input + ecx], bl
    inc ecx
    jmp reverse

print:
    mov [input + ecx], byte 10
    inc ecx
	mov eax, 4
	mov ebx, 1
	mov edx, ecx
	mov ecx, input
	int 0x80
	jmp return

checkSign:
    mov [sign], byte 1
    cmp eax, 0
    jl flipBits
    ret

flipBits:
    mov [sign], byte -1
    xor eax, -1
    inc eax
    ret

negArgs:
    push eax
    push ebx
    push ecx
    push edx
    cmp eax, 0
    jge return
    neg eax
    neg ebx
    jmp return

error:
	mov	eax, 4
	mov	ebx, 2
	mov	ecx, errorMsg
	mov	edx, lenError
	int	0x80
    mov ebx, 1
    jmp exit

printZero:
    mov eax, 4
    mov ebx, 1
    mov [input], byte 48
    mov [input + 1], byte 10
    mov ecx, input
    mov edx, 2
    int 0x80
    mov ebx, 0
    jmp return

exit:
	mov	eax, 1
	int	0x80
