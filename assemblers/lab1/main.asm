section	.data
    promptX	db 'Enter x: '
    lenX	equ	$ - promptX
    promptY db 'Enter y: '
    lenY equ $ - promptY
    errorMsg db 'ERROR: value must be an integer',10
    lenError equ $ - errorMsg
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
	mov esi, input
	xor eax, eax
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
	mov esi, input
	xor eax, eax
	call atoi
	mov [y], eax
	
	;Z = ((X+1)/Y - 1)*2X;
	mov eax, [x]
	inc eax
	xor edx, edx
	mov ebx, [y]
	div ebx
	dec eax
	call convertAndPrint
	mov ebx, 0
	call exit
	mov ebx, 2
	imul ebx
	mov ebx, [x]
	imul ebx
	
	call convertAndPrint
	mov ebx, 0
	call exit
	
atoi: ;converting string to number
    xor ecx, ecx
    MOV cl, byte [esi]
    CMP ecx, 10   ; newline character
    JE return
    CMP ecx, 48
    JL error
    CMP ecx, 57
    JG error
    MOV ebx, 10
    MUL ebx
    SUB ecx, 48
    ADD eax, ecx
    INC esi
    JMP atoi
	
return:
    ret
    
convertAndPrint:
    cmp eax, 0
    je printZero
    push 0
    xor ecx, ecx
itoa:
    cmp eax, 0
    je reverse
    xor edx, edx
    mov ebx, 10
    div ebx
	add edx, 48
	push edx
	jmp itoa
	
reverse:
    pop ebx
    cmp bl, 0
    je print
    mov [input + ecx], bl
    inc ecx
    jmp reverse
    
print:
    inc ecx
    mov [input + ecx], byte 10
	mov eax, 4
	mov ebx, 1
	xor edx, edx
	mov edx, ecx
	mov ecx, input
	int 0x80
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
    jmp exit
    
exit:
	mov	eax, 1	        ;system call number (sys_exit)
	int	0x80            ;call kernel