abstract sig Id {}
-- define um conjunto de variáveis
-- depois pode otimizar removendo essa assinatura
-- assuma que se tiver id, é por que tem um define associado
one sig Define {
	defs: set Id
}

abstract sig Variable {
	type: one Type-Void
}
sig LocalVar, GlobalVar extends Variable {}

sig Function {
	returnType: one Type,
	param: lone LocalVar,
	stmt: seq Stmt
}

-- descreve os fatos de boa formação
pred wf[]{
// se a função for void, não tem statement return
	all f:Function | 
		f.returnType = Void => no f.stmt.elems&Return 
// se a função não for void, tem um statement return no final
	all f:Function | 
		f.returnType != Void => f.stmt.last in Return 
// se a função não for void, só tem um statement return
	all f:Function | 
		f.returnType != Void => one f.stmt.elems & Return
// se a função não for void, o tipo de retorno é igual ao tipo da variavel do return
	all f:Function | 
		f.returnType != Void => f.stmt.last.r.type = f.returnType 

// todo id de um ifdef foi declarado por um define
	all x:ifDef | x.def in Define.defs

-- toda funcao que tiver uma atribuicao de variavel
-- essa variavel foi declarada no corpo ou no parametro
	all f:Function| 
		(all st1: f.stmt.elems | st1 in VarAttrib => 
			(some st2: f.stmt.elems | 
				st2 in LocalVarDecl and st2.(LocalVarDecl<:v) = st1.(VarAttrib<:v) and f.stmt.idxOf [st1] < f.stmt.idxOf [st2] ) or
			st1.(VarAttrib<:v) = f.param.v)
			
-- não há declaração de variável (LocalVarDecl) com mesmo nome de um param
	all f:Function | 
		(all lvd:f.stmt.elems | lvd in LocalVarDecl =>
 			(some p:f.param | lvd.(LocalVarDecl<:v) != p))		

-- não há declaração de variável (LocalVarDecl) com mesmo nome de outro LocalVarDecl
	all f:Function | 
		(all lvd:f.stmt.elems | lvd in LocalVarDecl =>
 			(some lvd2:f.stmt.elems | lvd.(LocalVarDecl<:v) != lvd2.(LocalVarDecl<:v)))
}

-- restrições para remover instâncias inválidas
pred optimization[] {
	Stmt in Function.stmt.elems + Function.stmt.elems.cmd
	Type in Variable.type+Function.returnType
	LocalVar in Function.param + LocalVarDecl.v
	Id in Define.defs
// vamos considerar sequencias de statements menores que 4
	all f: Function | #f.stmt < 4 
// vamos considerar pelo menos uma função com statement
	some f: Function | #f.stmt > 0
// vamos considerar que não temos elementos repetidos no corpo de uma funcao
	all f:Function | not f.stmt.hasDups
}

abstract sig Stmt {}
sig Return extends Stmt {
	r: Variable
}
sig LocalVarDecl extends Stmt {
	v: LocalVar
}

sig VarAttrib extends Stmt {
	v: Variable
}

sig ifDef extends Stmt {
	def: Id,
-- por simplicidade vamos evitar aninhamento de ifDefs e chamando return
	cmd: VarAttrib,
	else_: lone VarAttrib 
} 

-- descreve os tipos
abstract sig Type {}
lone sig Void extends Type {}
abstract sig BasicType extends Type {}
lone sig Integer_, Float extends BasicType {}

fact {
	wf[]
	optimization[]
}

-- pred show[] {
--	#Function =1
-- }
-- run show