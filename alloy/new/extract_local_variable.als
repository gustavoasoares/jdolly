open core
one sig varDecl extends LocalVarDecl{}
one sig func extends Function{}

one sig param extends LocalVar {}
one sig atrib extends  VarAttrib {}

pred extract_local_variable[] {
     varDecl in func.stmt.elems
atrib in func.stmt.elems
param in func.param
} 

run extract_local_variable for 2 but 3 Stmt
