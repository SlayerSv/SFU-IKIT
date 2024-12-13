section	.data
    promptX	db 'Enter x: '
    lenX	equ	$ - promptX
    inputFile db 'input.txt',0
    outputFile db 'output.txt',0
    inputFD dd 0
    outputFD dd 0
    stringSize equ 24
    intSize equ 4
    welderSize equ stringSize * 3 + intSize
    weldersCount equ 5
    o_creat equ 0q100
    o_trunc equ 0q1000
    o_wronly equ 0q1

section .bss
welders:
    resb welderSize * weldersCount

section	.text
	global _start

_start:
    mov	eax, 5
    mov	ebx, inputFile
    mov	ecx, 0
    int	0x80
    mov [inputFD], eax

    mov eax, 3
    mov ebx, [inputFD]
    mov ecx, welders
    mov edx, welderSize * weldersCount
    int 0x80

    mov eax, 6
    int 0x80

    call decreaseRank

    mov eax, 5
    mov ebx, outputFile
    mov ecx, o_creat | o_trunc | o_wronly
    mov edx, 0666
    int 0x80
    mov [outputFD], eax ;output file fd

    mov eax, 4
    mov ebx, [outputFD]
    mov ecx, welders
    mov edx, welderSize * weldersCount
    int 0x80

    call printWelders

    mov ebx, 0
    call exit

decreaseRank:
    push eax
    push ecx
    push esi
    mov esi, welders
    add esi, stringSize * 3
    xor ecx, ecx
    xor eax, eax
decreaseRankLoop:
    mov eax, [esi]
    dec eax
    mov [esi], eax
    inc ecx
    add esi, welderSize
    cmp ecx, weldersCount
    jl decreaseRankLoop
    pop esi
    pop ecx
    pop eax
    ret

printWelders:
    push eax
    push ebx
    push ecx
    push edx
    push esi
    xor ecx, ecx
    mov esi, welders

printWeldersLoop:
    mov [esi + (stringSize - 1)], byte 10
    mov [esi + (stringSize * 2 - 1)], byte 10
    mov [esi + (stringSize * 3 - 1)], byte 10
    mov eax, [esi + welderSize - 4]
    add eax, 48
    mov [esi + welderSize - 4], al
    mov [esi + welderSize - 3], byte 10
    add esi, welderSize
    inc ecx
    cmp ecx, weldersCount
    jl printWeldersLoop
    mov eax, 4
    mov ebx, 1
    mov ecx, welders
    mov edx, welderSize * weldersCount
    int 0x80
    pop esi
    pop edx
    pop ecx
    pop ebx
    pop eax
    ret

exit:
    mov	eax, 1
    int	0x80
