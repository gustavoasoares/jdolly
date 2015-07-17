open core
one sig identifier extends Id {}
pred rename_define[] {
	identifier in Define.defs
}
run rename_define for 2 but 3 Stmt
