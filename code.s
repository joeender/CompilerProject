#Prolog:
.text
.globl  main
main:
move  $fp  $sp #frame pointer will be start of active stack
la  $a0  ProgStart
li  $v0  4
syscall
#End of Prolog

li  $t0  1
sw  $t0  -8($fp)
lw  $t0  
sw  $t0  -4($fp)
li  $t0  1
sw  $t0  -12($fp)
lw  $t0  -12($fp)
lw  $t1  -4($fp)
add  $t2  $t0  $t1
sw  $t2  -16($fp)
lw  $t0  -16($fp)
sw  $t0  -0($fp)

#Postlog:
la  $a0  ProgEnd
li  $v0  4
syscall
li  $v0  10
syscall
.data
ProgStart:  .asciiz	"Program Start\n"
ProgEnd:   .asciiz	"Program  End\n"
