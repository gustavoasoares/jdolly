open core
one sig main extends Function {}
one sig return extends Return {}
one sig vardecl extends LocalVarDecl {}
one sig a,b extends LocalVar {}
one sig ifdef extends ifDef{}
one sig varAttrib1,varAttrib2 extends VarAttrib{}

fact {
main.param = a
main.stmt.first = vardecl
main.stmt.last = return
return.r=b
#main.stmt = 3
a.type = b.type
}
pred show[] {

}


run show for 1 Function, 2 Stmt, 1 Variable, 1 Define, 1 Id
