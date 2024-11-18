section	.data
    promptX	db 'Enter x: '
    lenX	equ	$ - promptX
    charZero DB 48
    errorMsg db 'ERROR: value must be an integer',10
    lenError equ $ - errorMsg
    
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
	
	;read entered value from user
	mov	eax, 3	        ;system call number (sys_read)
	mov	ebx, 0	        ;file descriptor (stdin)
	mov	ecx, input      ;message to write
	mov	edx, 10         ;message length
	int	0x80            ;call kernel
	mov esi, input
	xor eax, eax
	
atoi: ;converting string to number
    xor ecx, ecx
    MOV cl, byte [esi]
    CMP ecx, 10   ; newline character
    JE next
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
	
next:
    xor ecx, ecx
	push 0
itoa:
    cmp eax, 0
    je reverse
    mov edx, 0
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
	mov edx, ecx
	mov ecx, input
	int 0x80
	mov ebx, 0 ;set exit status code 'success'
	jmp exit
	
error:                  ;write error msg to stderr
	mov	eax, 4	        ;system call number (sys_write)
	mov	ebx, 2	        ;file descriptor (stderr)
	mov	ecx, errorMsg   ;message to write (user input)
	mov	edx, lenError   ;message length
	int	0x80            ;call kernel
    mov ebx, 1          ;set exit status code 'failure'
    
exit:
	mov	eax, 1	        ;system call number (sys_exit)
	int	0x80            ;call kernel
