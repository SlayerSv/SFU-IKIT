section	.data
    promptX	db 'Enter x: '
    lenX	equ	$ - promptX
    promptA db 'Enter a: '
    lenA equ $ - promptA
    errorMsg db 'ERROR: value must be a number',10
    lenError equ $ - errorMsg
    sign db 1
    multiplier dd 100.0
    four dd 4.0
    three dd 3.0
    one dd 1.0
    counter dd 0.0
    ten dd 10.0
    x dd 0
    a dd 0

section .bss
    input resb 10

section	.text
	global _start

;y = y1 + y2
;y1 = x % 4 if x > a, else a
;y2 = a * x if x / a > 3, else x
_start:
	mov	eax, 4
	mov	ebx, 1
	mov	ecx, promptX
	mov	edx, lenX
	int	0x80
	
	;read x value from user
	mov	eax, 3
	mov	ebx, 0
	mov	ecx, input
	mov	edx, 10
	int	0x80
	call atoi
	cvtsi2ss xmm0, eax
	movss xmm1, [multiplier]
	divss xmm0, xmm1
	movss [x], xmm0

	;message user to enter y value
	mov	eax, 4
	mov	ebx, 1
	mov	ecx, promptA
	mov	edx, lenA
	int	0x80

	;read a value from user
	mov	eax, 3
	mov	ebx, 0
	mov	ecx, input
	mov	edx, 10
	int	0x80
	call atoi
	cvtsi2ss xmm0, eax
	movss xmm1, [multiplier]
	divss xmm0, xmm1
	movss [a], xmm0

funcloop:
    call calc
    movss xmm4, [x]
    movss xmm5, [counter]
    addss xmm4, [one]
    addss xmm5, [one]
    movss [x], xmm4
    movss [counter], xmm5
    ucomiss xmm5, [ten]
    jb funcloop

    mov ebx, 0
	call exit

calc:
y1: ;y1 = x % 4 if x > a, else a
    ;compare x and a
    movss xmm0, [x]
    movss xmm1, [a]
    ucomiss xmm0, xmm1
    movss xmm0, xmm1 ;assume x <= a so set y1 as a
    jbe y2 ;and proceed to calculating y2
    
    ;if x > a calc x % 4
    movss xmm0, [x]
    movss xmm2, xmm0
    divss xmm2, [four]
    cvttss2si eax, xmm2
    cvtsi2ss xmm2, eax
    mulss xmm2, [four]
    subss xmm0, xmm2
    
y2: ;y2 = a * x if x / a > 3, else x
    movss xmm1, [x]
    movss xmm2, [a]
    movss xmm3, xmm1
    divss xmm3, xmm2
    ucomiss xmm3, [three]
    
    jbe y
    
    mulss xmm1, xmm2
	
y: ;y = y1 + y2
    addss xmm0, xmm1
    mulss xmm0, [multiplier]
    cvtss2si eax, xmm0
    call convertAndPrint
    ret

atoi:
    mov esi, input
	xor eax, eax
	xor ecx, ecx
	call setSign

atoiLoop:
    mov cl, byte [esi]
    cmp ecx, 46
    je skip
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

skip:
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
    ret

convertAndPrint:
    cmp eax, 0
    je printZero
    push 0
    xor ecx, ecx
    call checkSign

itoa:
    cmp ecx, 2
    je addDot
    cmp eax, 0
    je addSign
    xor edx, edx
    mov ebx, 10
    div ebx
	add edx, 48
	push edx
    inc ecx
	jmp itoa

addDot:
    push 46
    inc ecx
    jmp itoa

addSign:
    cmp ecx, 3
    je addZero
    xor ecx, ecx
    cmp [sign], byte 0
    jl addMinus
    jmp reverse

addMinus:
    push 45
	jmp reverse

addZero:
    push 48
    inc ecx
    jmp addSign

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
	ret

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
    cmp eax, 0
    jge return
    neg eax
    neg ebx
    ret

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
    mov [input + 1], byte 46
    mov [input + 2], byte 48
    mov [input + 3], byte 48
    mov [input + 4], byte 10
    mov ecx, input
    mov edx, 5
    int 0x80
    mov ebx, 0
    ret

exit:
	mov	eax, 1
	int	0x80
