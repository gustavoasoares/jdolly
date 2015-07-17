open core2
one sig varDecl extends LocalVarDecl{}
one sig func extends Function{}
pred rename_local_var[] {
     varDecl in func.stmt.elems
} 
run rename_local_var for 2 but 3 Stmt
