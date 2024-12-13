section	.data
    o_creat equ 0q100
    o_trunc equ 0q1000
    o_wronly equ 0q1
    fd dd 3
    bufSize equ 24
    inputFile db 'input.txt',0

    surname1 db 'Иванов',0
    name1 db "Иван",0
    patronymic1 db "Иванович",0
    rank1 dd 4

    surname2 db 'Петров',0
    name2 db "Петр",0
    patronymic2 db "Петрович",0
    rank2 dd 3

    surname3 db 'Сидоров',0
    name3 db "Сидор",0
    patronymic3 db "Сидорович",0
    rank3 dd 5

    surname4 db 'Романов',0
    name4 db "Роман",0
    patronymic4 db "Романович",0
    rank4 dd 6

    surname5 db 'Николаев',0
    name5 db "Николай",0
    patronymic5 db "Николаевич",0
    rank5 dd 2

section .bss
    buffer resb bufSize

section	.text
	global _start

_start:
    mov eax, 5
    mov ebx, inputFile
    mov ecx, o_creat | o_trunc | o_wronly
    mov edx, 0666
    int 0x80

    mov ebx, eax
    mov edi, buffer

    call fillZeroes
    mov esi, surname1
    call copy
    mov eax, 4
    mov ecx, buffer
    mov edx, bufSize
    int 0x80

    call fillZeroes
    mov esi, name1
    call copy
    mov eax, 4
    int 0x80

    call fillZeroes
    mov esi, patronymic1
    call copy
    mov eax, 4
    int 0x80

    mov eax, 4
    mov ecx, rank1
    mov edx, 4
    int 0x80

    call fillZeroes
    mov esi, surname2
    call copy
    mov eax, 4
    mov ecx, buffer
    mov edx, bufSize
    int 0x80

    call fillZeroes
    mov esi, name2
    call copy
    mov eax, 4
    int 0x80

    call fillZeroes
    mov esi, patronymic2
    call copy
    mov eax, 4
    int 0x80

    mov eax, 4
    mov ecx, rank2
    mov edx, 4
    int 0x80

    call fillZeroes
    mov esi, surname3
    call copy
    mov eax, 4
    mov ecx, buffer
    mov edx, bufSize
    int 0x80

    call fillZeroes
    mov esi, name3
    call copy
    mov eax, 4
    int 0x80

    call fillZeroes
    mov esi, patronymic3
    call copy
    mov eax, 4
    int 0x80

    mov eax, 4
    mov ecx, rank3
    mov edx, 4
    int 0x80

    call fillZeroes
    mov esi, surname4
    call copy
    mov eax, 4
    mov ecx, buffer
    mov edx, bufSize
    int 0x80

    call fillZeroes
    mov esi, name4
    call copy
    mov eax, 4
    int 0x80

    call fillZeroes
    mov esi, patronymic4
    call copy
    mov eax, 4
    int 0x80

    mov eax, 4
    mov ecx, rank4
    mov edx, 4
    int 0x80

    call fillZeroes
    mov esi, surname5
    call copy
    mov eax, 4
    mov ecx, buffer
    mov edx, bufSize
    int 0x80

    call fillZeroes
    mov esi, name5
    call copy
    mov eax, 4
    int 0x80

    call fillZeroes
    mov esi, patronymic5
    call copy
    mov eax, 4
    int 0x80

    mov eax, 4
    mov ecx, rank5
    mov edx, 4
    int 0x80

    mov ebx, 0
    mov eax, 1
    int 0x80


fillZeroes:
    push ecx
    xor ecx, ecx
fillZeroesLoop:
    mov [edi + ecx], byte 0
    inc ecx
    cmp ecx, bufSize
    jl fillZeroesLoop
    pop ecx
    ret

copy:
    push ecx
    push ebx
    xor ecx, ecx
copyLoop:
    mov bl, byte [esi + ecx]
    mov [edi + ecx], bl
    inc ecx
    cmp [esi + ecx], byte 0
    jne copyLoop
    pop ebx
    pop ecx
    ret
