open core
one sig main extends Function {}
one sig return extends Return {}
one sig a extends LocalVar {}

fact {
}
pred show[] {
main.param = a
a.type=Integer_
main.stmt.last = return
#main.stmt = 2
}


run show for 2 but 3 Stmt
