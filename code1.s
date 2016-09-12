#Prolog:
.text
.globl  main
main:
move  $fp  $sp #frame pointer will be start of active stack
la  $a0  ProgStart
li  $v0  4
syscall
#End of Prolog

li  $t0  0
sw  $t0  -0($fp)
j  MAIN
isBigger:
li  $t3  4
lw  $t1  -0($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -8($fp)
sub $t1  $t2  $t1
sw  $s0  0($t1)
li  $t3  4
lw  $t1  -0($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -12($fp)
sub $t1  $t2  $t1
sw  $s1  0($t1)
lw  $t0  -8($fp)
lw  $t1  -12($fp)
slt  $t2  $t0  $t1
sw  $t2  -16($fp)
lw  $t0  -8($fp)
lw  $t1  -12($fp)
seq  $t2  $t1  $t0
sw  $t2  -20($fp)
lw  $t0  -16($fp)
lw  $t1  -20($fp)
or  $t2  $t1  $t0
sw  $t2  -24($fp)
lw  $t0  -24($fp)
beqz  $t0  endIF0
li  $t0  0
sw  $t0  -28($fp)
lw  $t0  -28($fp)
sw  $t0  4($sp)
jr  $ra
endIF0:
li  $t0  1
sw  $t0  -32($fp)
lw  $t0  -32($fp)
sw  $t0  4($sp)
jr  $ra
MAIN:
li  $t0  10
sw  $t0  -40($fp)
li  $t0  0
sw  $t0  -92($fp)
li  $t3  4
lw  $t1  -0($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -80($fp)
sub $t1  $t2  $t1
lw  $t0  -92($fp)
sw  $t0  0($t1)
li  $t0  1
sw  $t0  -96($fp)
li  $t3  4
lw  $t1  -0($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -84($fp)
sub $t1  $t2  $t1
lw  $t0  -96($fp)
sw  $t0  0($t1)
li  $t0  1
sw  $t0  -104($fp)
li  $t3  4
lw  $t1  -0($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -100($fp)
sub $t1  $t2  $t1
lw  $t0  -104($fp)
sw  $t0  0($t1)
li  $t0  0
sw  $t0  -112($fp)
li  $t3  4
lw  $t1  -0($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -108($fp)
sub $t1  $t2  $t1
lw  $t0  -112($fp)
sw  $t0  0($t1)
li  $t0  0
sw  $t0  -116($fp)
li  $t0  9
sw  $t0  -120($fp)
li  $t3  4
lw  $t1  -116($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -36($fp)
sub $t1  $t2  $t1
lw  $t0  -120($fp)
sw  $t0  0($t1)
li  $t0  1
sw  $t0  -124($fp)
li  $t0  15
sw  $t0  -128($fp)
li  $t3  4
lw  $t1  -124($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -36($fp)
sub $t1  $t2  $t1
lw  $t0  -128($fp)
sw  $t0  0($t1)
li  $t0  2
sw  $t0  -132($fp)
li  $t0  10
sw  $t0  -136($fp)
li  $t3  4
lw  $t1  -132($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -36($fp)
sub $t1  $t2  $t1
lw  $t0  -136($fp)
sw  $t0  0($t1)
li  $t0  3
sw  $t0  -140($fp)
li  $t0  4
sw  $t0  -144($fp)
li  $t3  4
lw  $t1  -140($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -36($fp)
sub $t1  $t2  $t1
lw  $t0  -144($fp)
sw  $t0  0($t1)
li  $t0  4
sw  $t0  -148($fp)
li  $t0  2
sw  $t0  -152($fp)
li  $t3  4
lw  $t1  -148($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -36($fp)
sub $t1  $t2  $t1
lw  $t0  -152($fp)
sw  $t0  0($t1)
li  $t0  5
sw  $t0  -156($fp)
li  $t0  3
sw  $t0  -160($fp)
li  $t3  4
lw  $t1  -156($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -36($fp)
sub $t1  $t2  $t1
lw  $t0  -160($fp)
sw  $t0  0($t1)
li  $t0  6
sw  $t0  -164($fp)
li  $t0  1
sw  $t0  -168($fp)
li  $t3  4
lw  $t1  -164($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -36($fp)
sub $t1  $t2  $t1
lw  $t0  -168($fp)
sw  $t0  0($t1)
li  $t0  7
sw  $t0  -172($fp)
li  $t0  8
sw  $t0  -176($fp)
li  $t3  4
lw  $t1  -172($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -36($fp)
sub $t1  $t2  $t1
lw  $t0  -176($fp)
sw  $t0  0($t1)
li  $t0  8
sw  $t0  -180($fp)
li  $t0  8
sw  $t0  -184($fp)
li  $t3  4
lw  $t1  -180($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -36($fp)
sub $t1  $t2  $t1
lw  $t0  -184($fp)
sw  $t0  0($t1)
li  $t0  9
sw  $t0  -188($fp)
li  $t0  6
sw  $t0  -192($fp)
li  $t3  4
lw  $t1  -188($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -36($fp)
sub $t1  $t2  $t1
lw  $t0  -192($fp)
sw  $t0  0($t1)
beginWHILE0:
lw  $t0  -100($fp)
beqz  $t0  endWHILE0
li  $t0  0
sw  $t0  -196($fp)
li  $t3  4
lw  $t1  -0($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -100($fp)
sub $t1  $t2  $t1
lw  $t0  -196($fp)
sw  $t0  0($t1)
beginWHILE1:
li  $t0  9
sw  $t0  -200($fp)
lw  $t0  -80($fp)
lw  $t1  -200($fp)
slt  $t2  $t0  $t1
sw  $t2  -204($fp)
lw  $t0  -204($fp)
beqz  $t0  endWHILE1
li  $t3  4
lw  $t1  -80($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -36($fp)
sub  $t1  $t2  $t1
lw  $t4  0($t1)
sw  $t4  -208($fp)
lw  $s0  -208($fp)
li  $t3  4
lw  $t1  -84($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -36($fp)
sub  $t1  $t2  $t1
lw  $t4  0($t1)
sw  $t4  -212($fp)
lw  $s1  -212($fp)
jal  isBigger
lw  $t0  4($sp)
sw  $t0  -216($fp)
lw  $t0  -216($fp)
beqz  $t0  endIF1
li  $t3  4
lw  $t1  -84($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -36($fp)
sub  $t1  $t2  $t1
lw  $t4  0($t1)
sw  $t4  -220($fp)
li  $t3  4
lw  $t1  -0($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -88($fp)
sub $t1  $t2  $t1
lw  $t0  -220($fp)
sw  $t0  0($t1)
li  $t3  4
lw  $t1  -80($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -36($fp)
sub  $t1  $t2  $t1
lw  $t4  0($t1)
sw  $t4  -224($fp)
li  $t3  4
lw  $t1  -84($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -36($fp)
sub $t1  $t2  $t1
lw  $t0  -224($fp)
sw  $t0  0($t1)
li  $t3  4
lw  $t1  -80($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -36($fp)
sub $t1  $t2  $t1
lw  $t0  -88($fp)
sw  $t0  0($t1)
li  $t0  1
sw  $t0  -228($fp)
li  $t3  4
lw  $t1  -0($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -100($fp)
sub $t1  $t2  $t1
lw  $t0  -228($fp)
sw  $t0  0($t1)
endIF1:
li  $t0  1
sw  $t0  -232($fp)
lw  $t0  -80($fp)
lw  $t1  -232($fp)
add  $t2  $t0  $t1
sw  $t2  -236($fp)
li  $t3  4
lw  $t1  -0($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -80($fp)
sub $t1  $t2  $t1
lw  $t0  -236($fp)
sw  $t0  0($t1)
li  $t0  1
sw  $t0  -240($fp)
lw  $t0  -84($fp)
lw  $t1  -240($fp)
add  $t2  $t0  $t1
sw  $t2  -244($fp)
li  $t3  4
lw  $t1  -0($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -84($fp)
sub $t1  $t2  $t1
lw  $t0  -244($fp)
sw  $t0  0($t1)
j beginWHILE1
endWHILE1:
li  $t0  1
sw  $t0  -248($fp)
lw  $t0  -100($fp)
lw  $t1  -248($fp)
seq  $t2  $t1  $t0
sw  $t2  -252($fp)
lw  $t0  -252($fp)
beqz  $t0  endIF2
li  $t0  0
sw  $t0  -256($fp)
li  $t3  4
lw  $t1  -0($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -80($fp)
sub $t1  $t2  $t1
lw  $t0  -256($fp)
sw  $t0  0($t1)
li  $t0  1
sw  $t0  -260($fp)
li  $t3  4
lw  $t1  -0($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -84($fp)
sub $t1  $t2  $t1
lw  $t0  -260($fp)
sw  $t0  0($t1)
endIF2:
j beginWHILE0
endWHILE0:
beginWHILE2:
li  $t0  10
sw  $t0  -264($fp)
lw  $t0  -108($fp)
lw  $t1  -264($fp)
slt  $t2  $t0  $t1
sw  $t2  -268($fp)
lw  $t0  -268($fp)
beqz  $t0  endWHILE2
li  $t3  4
lw  $t1  -108($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -36($fp)
sub  $t1  $t2  $t1
lw  $t4  0($t1)
sw  $t4  -272($fp)
lw  $a0  -272($fp)
li  $v0  1
syscall
la  $a0  NewLine
li  $v0  4
syscall
li  $t0  1
sw  $t0  -276($fp)
lw  $t0  -108($fp)
lw  $t1  -276($fp)
add  $t2  $t0  $t1
sw  $t2  -280($fp)
li  $t3  4
lw  $t1  -0($fp)
mult  $t3  $t1
mflo  $t1
la  $t2  -108($fp)
sub $t1  $t2  $t1
lw  $t0  -280($fp)
sw  $t0  0($t1)
j beginWHILE2
endWHILE2:

#Postlog:
la  $a0  ProgEnd
li  $v0  4
syscall
li  $v0  10
syscall
.data
ProgStart:  .asciiz	"Program Start\n"
ProgEnd:   .asciiz	"Program  End\n"
NewLine:   .asciiz	"\n"