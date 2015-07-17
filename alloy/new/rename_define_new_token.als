open core

one sig main extends Function {}
one sig return extends Return {}
one sig vardecl extends LocalVarDecl {}
one sig atrib extends VarAttrib{}
one sig ifdef_ extends ifDef {}
one sig A extends Id {}

fact {
-- define
    Define.defs = A

-- definicao do corpo da funcao
    #main.stmt = 3

    main.stmt.first = vardecl
-- dica para pegar outros elementos da lista: chama rest e depois first
    main.stmt.rest.first = ifdef_
    main.stmt.last = return

-- definicao do ifdef
    ifdef_.def = A
    ifdef_.cmd = atrib
    no ifdef_.else_

-- definicao de atrib
    atrib.v = vardecl.v
}
pred show[] {}
run show for 1 Function, 4 Stmt, 2 Variable, 1 Define, 1 Id