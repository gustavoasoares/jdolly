open core
one sig main extends Function {}
one sig return extends Return {}
one sig aa extends LocalVar {}
one sig AA extends Id {}

fact {
    Define.defs = AA

    main.param = aa
    main.stmt.first = return
    #main.stmt = 1
}
pred show[] {
   
}
run show for 1 Function, 2 Stmt, 1 Variable, 1 Define, 1 Id