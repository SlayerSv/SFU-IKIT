section	.data
    dimension db 5
    ;matrix values must be between 0 and 9
    matrix db 1, 2, 3, 4, 5, 6, 7, 8, 9, 9, 8, 7, 6, 5, 4, 3, 2, 1, 1, 2, 3, 4, 5, 6, 7
    matrixSize equ $ - matrix
    outputSize db 0
    msg1 db 'Initial matrix:', 10
    msg1Len equ $ - msg1
    msg2 db 'Modified matrix:', 10
    msg2Len equ $ - msg2
    sentencePrompt db 'Enter any sentence: '
    sentencePromptLen equ $ - sentencePrompt

section .bss
    output resb 200
    input resb 200

section	.text
	global _start

_start:
    ;print message 'Initial matrix:'
	mov eax, 4
	mov ebx, 1
	mov ecx, msg1
	mov edx, msg1Len
	int 0x80

    ;convert matrix to string
	call itoaMatrix
    
    ;print matrix as string
    mov eax, 4
    mov ebx, 1
    mov ecx, output
    movzx edx, byte [outputSize]
    int 0x80
    
    ;modify matrix replacing certain cells with 0
    call iterateMatrix
    
    ;print message 'Modified matrix:'
    mov eax, 4
	mov ebx, 1
	mov ecx, msg2
	mov edx, msg2Len
	int 0x80
    
    ;convert modified matrix to string
    call itoaMatrix

    ;print modified matrix as string
    mov eax, 4
    mov ebx, 1
    mov ecx, output
    movzx edx, byte [outputSize]
    int 0x80
    
    ;print message 'Enter any sentence'
    mov eax, 4
    mov ebx, 1
    mov ecx, sentencePrompt
    mov edx, sentencePromptLen
    int 0x80
    
    ;read user input
    mov eax, 3
    mov ebx, 0
    mov ecx, input
    mov edx, 200
    int 0x80

    mov edx, eax ;save in edx length of user input
    mov esi, input ;move user input pointer to esi
    
findSpace: ;move pointer until space or new line is found
    movzx ebx, byte [esi]
    cmp ebx, 10
    je printSentence
    dec edx ;decrease length of user input
    inc esi
    cmp ebx, ' '
    je printSentence
    jmp findSpace
    
printSentence: ;print user input with first word removed
    mov eax, 4
    mov ebx, 1
    mov ecx, esi
    int 0x80
    
	mov ebx, 0
	call exit

iterateMatrix:
    push eax
    push ebx
    push ecx
    push edx
    xor ecx, ecx ;row counter
    xor edx, edx ;col counter
    movzx ebx, byte [dimension] ;dimension of the matrix
    
iterateMatrixLoop:
    cmp ecx, ebx ;check if row counter == dimension (iteration is done)
    je return
    call checkMainDiagonal
    inc edx ;increase col counter
    cmp edx, ebx ;check if col counter == dimension (need to move to new row)
    je nextRow
    jmp iterateMatrixLoop
    
nextRow:
    inc ecx ;increase row counter
    mov edx, 0 ;col counter = 0
    jmp iterateMatrixLoop

checkMainDiagonal: ;check if cell is above main diagonal
    cmp edx, ecx
    jge checkSecondaryDiagonal
    ret
    
checkSecondaryDiagonal: ;check if cell is above secondary diagonal
    push ebx
    sub ebx, ecx
    cmp edx, ebx
    pop ebx
    jl zeroCell
    ret

zeroCell: ;if cell is above main and secondary diagonal put 0 in this cell
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

itoaMatrix: ;convert matrix to string
    push eax
    push ebx
    push ecx
    push edx
    xor ecx, ecx ;matrix cell pointer (counter)
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

checkEndOfRow: ;if end of row is reached push new line character
    push eax
    push ebx
    push ecx
    push edx
    xor edx, edx
    mov eax, ecx
    movzx ebx, byte [dimension]
    div ebx ;cell counter / dimension
    cmp edx, 0 ;check if cell counter % dimension == 0 (to push new line)
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
