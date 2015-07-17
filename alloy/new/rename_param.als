open core
one sig main extends Function {}
one sig param extends LocalVar {}
pred rename_param[] {
    param in main.param
}
run rename_param for 2 but 3 Stmt