/**

The C Language metamodel CORE.

This is a simplified metamodel for the C language

Author:
	Jeanderson Candido
	http://jeandersonbc.github.io

*/

// ENTITIES
abstract sig Identifier {}
sig GlobalVariableId, FunctionId, LocalVariableId extends Identifier {}

// Declaration is either a Variable, Function, or a Struct (a short for structure) declaration
abstract sig Declaration {}
sig TranslationUnit {
	declares: set Declaration
}

sig GlobalVariable extends Declaration {
	type: one BasicType,
	id: one GlobalVariableId,
}

sig Function extends Declaration {
	id: one FunctionId,
	returnType: one Type,
	param: lone LocalVariable,

	declares: set LocalVariable,

	returnStmt: lone ReturnStmt
}

sig LocalVariable {
	id: one LocalVariableId,
	type: one BasicType,
}

// RETURN STATEMENTS
abstract sig ReturnStmt {
	type: one BasicType
}
lone sig ReturnLiteral extends ReturnStmt {}

// TYPES
abstract sig Type {}
abstract sig BasicType extends Type {}
lone sig Void extends Type {}
lone sig Integer, Float, Double, Char extends BasicType {}

// CONSTRAINTS
fact wellFormedProgram {
	translationUnitNotEmpty
	allIdentifiersAreUnique
	allLocalVariablesExistInFunction
	allDeclarationsExistInTranslationUnit
	allTypesExistSomewhere
	allReturnStmtsExistInFunction
	voidFunctionsDontHaveReturnStmt
 	parametersAndVariablesDisjoint
	nonVoidFunctionsHaveReturnStmt
}

pred nonVoidFunctionsHaveReturnStmt {
	all f:Function |
		(f.returnType != Void) => f.returnStmt .type = f.returnType
}

pred voidFunctionsDontHaveReturnStmt {
	all f:Function |
		(f.returnType = Void) => #f.returnStmt  = 0
}

pred allReturnStmtsExistInFunction {
	all r:ReturnStmt | one f:Function | r in f.returnStmt
}

pred allDeclarationsExistInTranslationUnit {
	all d:Declaration | one src:TranslationUnit | d in src.declares
}

pred parametersAndVariablesDisjoint {
	all f:Function |
		(#f.param > 0) => #(f.param & f.declares) = 0
}

pred allLocalVariablesExistInFunction {
	all var:LocalVariable |
		var in Function.param or
		var in Function.declares
}

pred allIdentifiersAreUnique {
	all id1:LocalVariableId | one local:LocalVariable | id1 in local.id	
	all id2:GlobalVariableId | one g:GlobalVariable | id2 in g.id
	all id3:FunctionId | one f:Function| id3 in f.id
}

pred translationUnitNotEmpty {
	all src:TranslationUnit | #src.declares > 0
}

pred allTypesExistSomewhere {
	all t:Type |
		t in Function.returnType or
		t in GlobalVariable.type or
		t in LocalVariable.type
}
