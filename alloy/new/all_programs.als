open core
--one sig main extends Function {}
--one sig a extends LocalVar {}
--fact {
--	a in main.param
--}
--pred show[] {}

-- run show for 1 Function, 2 Stmt, 1 Variable, 1 Define, 1 Id

pred show[] {
--	#Function =1
}
run show for 3
