open core
one sig main extends Function {}
one sig return extends Return {}
one sig vardecl extends LocalVarDecl {}
one sig aa,bb extends LocalVar {}
fact {
main.param = aa
main.stmt.first = vardecl
main.stmt.last = return
#main.stmt = 2
aa.type = bb.type
}
pred show[] {

}


run show for 1 Function, 2 Stmt, 1 Variable, 1 Define, 1 Id