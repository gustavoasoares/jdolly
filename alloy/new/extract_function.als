open core
one sig varDecl extends LocalVarDecl{}
one sig func extends Function{}
pred extract_function[] {
     varDecl in func.stmt.elems
} 
run extract_function for 2 but 3 Stmt
