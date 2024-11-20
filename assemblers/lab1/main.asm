section	.data
    promptX	db 'Enter x: '
    lenX	equ	$ - promptX
    promptY db 'Enter y: '
    lenY equ $ - promptY
    errorMsg db 'ERROR: value must be an integer',10
    lenError equ $ - errorMsg
    sign db 1
    x dd 0
    y dd 0
    z dd 0

section .bss
    input resb 10
    
section	.text
	global _start       ;must be declared for using gcc
_start:                 ;tell linker entry point
	;message user to enter x value
	mov	eax, 4	        ;system call number (sys_write)
	mov	ebx, 1	        ;file descriptor (stdout)
	mov	ecx, promptX    ;message to write
	mov	edx, lenX       ;message length
	int	0x80            ;call kernel
	
	;read x value from user
	mov	eax, 3	        ;system call number (sys_read)
	mov	ebx, 0	        ;file descriptor (stdin)
	mov	ecx, input      ;buffer to write
	mov	edx, 10         ;message length
	int	0x80            ;call kernel
	call atoi
	mov [x], eax

	;message user to enter y value
	mov	eax, 4	        ;system call number (sys_write)
	mov	ebx, 1	        ;file descriptor (stdout)
	mov	ecx, promptY    ;message to write
	mov	edx, lenY       ;message length
	int	0x80            ;call kernel
	
	;read y value from user
	mov	eax, 3	        ;system call number (sys_read)
	mov	ebx, 0	        ;file descriptor (stdin)
	mov	ecx, input      ;buffer to write
	mov	edx, 10         ;message length
	int	0x80            ;call kernel
	call atoi
	mov [y], eax
	
	
	
	;Z = ((X+1)/Y - 1)*2X;
	mov eax, [x]
	inc eax
	mov ebx, [y]
	xor edx, edx
	call negArgs
	idiv ebx
	dec eax
	mov ebx, 2
	imul ebx
	mov ebx, [x]
	imul ebx
	call convertAndPrint
	
	 ;Z = Y*( 2-(Y+1)/X )
	mov eax, [y]
	inc eax
	mov ebx, [x]
	xor edx, edx
	call negArgs
	idiv ebx
	mov ebx, 2
	sub ebx, eax
	mov eax, [y]
	imul ebx
	call convertAndPrint
	
	;Z = (XY - 1)/(X+Y);
	mov eax, [x]
	mov ebx, [y]
	imul ebx
	dec eax
	mov ebx, [x]
	add ebx, [y]
	call negArgs
	xor edx, edx
	idiv ebx
	call convertAndPrint
	
	;Z = X^3 + Y -1;
	mov eax, [x]
	imul dword [x]
	imul dword [x]
	add eax, [y]
	dec eax
	call convertAndPrint
	
	;Z = (XY + 1)/ X^2
	mov eax, [x]
	imul dword [y]
	inc eax
	mov ebx, [x]
	imul ebx, ebx
	xor edx, edx
	call negArgs
	idiv ebx
	call convertAndPrint
	
	mov ebx, 0
	call exit
	
atoi: ;converting string to number
    mov esi, input
	xor eax, eax
	xor ecx, ecx
	call setSign
	
atoiLoop:
    MOV cl, byte [esi]
    CMP ecx, 10   ; newline character
    JE negate
    CMP ecx, 48
    JL error
    CMP ecx, 57
    JG error
    MOV ebx, 10
    MUL ebx
    SUB ecx, 48
    ADD eax, ecx
    INC esi
    JMP atoiLoop

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

error:                  ;write error msg to stderr
	mov	eax, 4	        ;system call number (sys_write)
	mov	ebx, 2	        ;file descriptor (stderr)
	mov	ecx, errorMsg   ;message to write (user input)
	mov	edx, lenError   ;message length
	int	0x80            ;call kernel
    mov ebx, 1          ;set exit status code 'failure'
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
    ret

exit:
	mov	eax, 1	        ;system call number (sys_exit)
	int	0x80            ;call kernel
