open core
one sig varDecl extends LocalVarDecl{}
one sig func extends Function{}
pred extract_constant[] {
     varDecl in func.stmt.elems
} 
run extract_constant for 2 but 3 Stmt
