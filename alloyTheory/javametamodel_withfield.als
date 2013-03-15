module javametamodel_withfield
// ABSTRACT SYNTAX

abstract sig Id {}

sig Package{} 

sig ClassId, MethodId,FieldId extends Id {}

abstract sig Accessibility {}

one sig public, private_, protected extends Accessibility {}

abstract sig Type {}

abstract sig PrimitiveType extends Type {}

one sig Int_, Long_ extends PrimitiveType {}

sig Class extends Type {
	package: one Package,
	id: one ClassId,
	extend: lone Class,
	methods: set Method,
	fields: set Field
} 

fun classes[pack:Package]: set Class {
	pack.~package
}

sig Field {
    id : one FieldId,
    type: one Type,
    acc : lone Accessibility 
}

sig Method {
	id : one MethodId,
    param: lone Type,
    acc: lone Accessibility,
    return: one Type, 
    b: one Body
} 

abstract sig Body {}

sig LiteralValue extends Body {} // returns a random value

abstract sig Qualifier {}

one sig qthis_, this_, super_ extends Qualifier {}


sig MethodInvocation extends Body {
    id : one MethodId,
    q: lone Qualifier 
}
fact {
// call a declared method
    all mi:MethodInvocation | some m:Method | mi.id = m.id
// avoid recursive calls
    all m:Method | all mb: MethodInvocation | m.b = mb => mb.id != m.id    
}

//        return new A().k();
sig ConstructorMethodInvocation extends Body {
    idClass : one ClassId,
    idMethod: one MethodId
}
fact {
// calls a method declared in the class
    all ci: ConstructorMethodInvocation |
        some c:Class |
            ci.idClass = c.id &&
            (some m:Method | m in c.methods && m.id = ci.idMethod)  

// avoid recursive calls
    all m:Method | all mb: ConstructorMethodInvocation | m.b = mb => mb.idMethod != m.id
}

fun classFromClassId[id1:ClassId]: set Class {
	id1.~id
}

fun fieldFromFieldId[id1:FieldId]: set Field {
	id1.~id
}

//        return x;
//        return this.x;
//        return super.x;
//        return A.this.x; -> implement in Java whether use this or qualified this
sig FieldInvocation extends Body {
    idField : one FieldId,
    qField: lone Qualifier
}

//        return new A().x;
sig ConstructorFieldInvocation extends Body {
    idClass2 : one ClassId,
    idField: one FieldId
}
fact {
    //calss a field declared in the class
    all ci: ConstructorFieldInvocation | 
        some c:Class |
            ci.idClass2 = c.id &&
           (some f:Field | f in c.fields && f.id = ci.idField)
}



// WELL-FORMED RULES
fact JavaWellFormedRules {
	noPackageContainsTwoClassesWithSameId[]
   noCalltoUndefinedField[]
    noSuperCallToNotInheritedField[]

    noClassExtendsItself[]
    allFieldsBelongToAClass[]
	noClassContainsTwoFieldsWithSameId []
	noClassContainsTwoMethodsWithSameSignature[]
	noClassExtendsAnotherWithSameId[]	
	allBodiesBelongToAMethod[]
    allMethodsBelongToAClass[]
    noSuperCallToNotInheritedMethod[]
    noCalltoUndefinedMethod[]
}
pred noPackageContainsTwoClassesWithSameId[] {
	all package: Package | all c1,c2:classes[package] | c1!=c2 => c1.id != c2.id
}

pred noClassExtendsItself[] {
	no c:Class | c in c.^extend
}

pred noClassExtendsAnotherWithSameId[] {
	all c1:Class | no c2: c1.^extend | c1.id = c2.id
}

pred noClassContainsTwoFieldsWithSameId [] {
    no c:Class | some disj f1,f2:Field | f1.id = f2.id && f1 + f2 in c.fields
}

pred noCalltoUndefinedMethod[] {
  all mi:MethodInvocation | 
      (#mi.q = 0 || mi.q = this_) => 
          some c1,c2: Class, m1:c1.methods, m2:c2.methods  | mi in m1.b && mi.id = m2.id && ((c1 = c2) || ((c2 in c1.^extend) && (m2.acc != private_)))

  all mi:MethodInvocation | 
      (mi.q = qthis_) => 
          some c1:Class, m1,m2:c1.methods | mi in m1.b && mi.id = m2.id 


}

pred noSuperCallToNotInheritedMethod[] {
   all mi:MethodInvocation | 
       mi.q = super_ => 
          some c1,c2: Class, m1:c1.methods, m2:c2.methods |  mi in m1.b && mi.id = m2.id && c2 in c1.^extend && (m2.acc != private_)
}

pred noSuperCallToNotInheritedField[] {
  all fi:FieldInvocation | 
    fi.qField = super_ => 
      some disj c1,c2: Class, m1:c1.methods, f:c2.fields |  fi in m1.b && fi.idField = f.id && c2 in c1.^extend && f.acc != private_

 
}

pred noCalltoUndefinedField[] {
 all mi:FieldInvocation | 
     ( mi.qField = this_) => 
          some c1,c2: Class, m1:c1.methods, f:c2.fields | mi in m1.b && mi.idField = f.id && ((c1 = c2) || ((c2 in c1.^extend) && (f.acc != private_)))

  all mi:FieldInvocation | 
      ( mi.qField = qthis_) => 
          some c1,c2: Class, m1:c1.methods, f:c2.fields | mi in m1.b && mi.idField = f.id && ((c1 = c2) || ((c2 in c1.^extend) && (f.acc != private_)))

  all mi:FieldInvocation | 
      ( #mi.qField = 0) => 
          some c1,c2: Class, m1:c1.methods, f:c2.fields | mi in m1.b && mi.idField = f.id && ((c1 = c2) || ((c2 in c1.^extend) && (f.acc != private_)))
}

pred allFieldsBelongToAClass [] {
    all f:Field | one c:Class | f in c.fields
}

pred noClassContainsTwoMethodsWithSameSignature[] {
	all c: Class | all m1,m2:c.methods | m1!=m2 =>(m1.id != m2.id or m1.param != m2.param)
}

pred allMethodsBelongToAClass [] {
    all m:Method | one c:Class | m in c.methods
}

pred allBodiesBelongToAMethod [] {
   Body in Method.b
}

//OPTIMIZATIONS

fact jdolly {
//return type myu
all m:Method | m.return = Long_

//param must be primitve
no m:Method | #m.param = 1 && m.param ! in PrimitiveType 

//do not generate empty package
Package in Class.package

//only generates ClassId when there is a class, the same for methods
all id1:ClassId | some c:Class | c.id = id1
all id1:MethodId| some m:Method | m.id = id1

FieldId in Field.id

//one body per method 
no m1,m2:Method | m1!= m2 && m1.b = m2.b
}


//daqui pra baixo, os predicados sao opcionais
pred twoDistinctFields {
some disj f1,f2:Field | f1.id != f2.id && f1.type + f2.type in Int_
}

//THE FOLLOWING PREDICATES ARE USED TO TEST SOME REFACTORING IMPLEMENTATIONS

pred differentIdsForClasses[] {
all cid:ClassId | one c:Class | c.id = cid
}

pred somePrimitiveFields[] {
  some disj f1,f2:Field | class[f1] != class[f2]
  Field.type = Int_
  #FieldId = 2
}

pred someMain[] {
  one m: Method | m.acc = public  && #m.param = 0 && m.b !in LiteralValue 
}


pred someFieldhiding[] {
some disj f1,f2:Field | f1.id = f2.id && f1.type + f2.type in Int_ //&& sameHierarchy[f1,f2]
}


pred someMain[id1: MethodId] {
one m: Method | m.acc = public  && #m.param = 0 && m.b in MethodInvocation && #m.b.q = 0  && m.b.id = id1
&& methodNotCalled[m] 
}

pred someInheritance[]  {
  some disj c1,c2:Class | c1 in c2.extend
}
pred caller[m:Method] {
  m.acc = public  && #m.param = 0 && m.b !in LiteralValue //&& methodNotCalled[m] 
}


pred methodMain[id1: MethodId] {
one m: Method | m.acc = public  && #m.param = 0 && m.b in MethodInvocation && #m.b.q = 0  && m.b.id = id1
&& methodNotCalled[m] 
}

pred methodNotCalled[m1:Method] {
no mi:MethodInvocation| mi.id = m1.id 
no cmi:ConstructorMethodInvocation | cmi.idMethod = m1.id
}



//AUXILIAR FUNCTIONS
fun class[f1:Field]: one Class {
	f1.~fields
}



pred show[] {
}

//numero de classes, metodos, e atributos 
assert test1 {
#Package >= 0
#Class >= 0 
#Method >= 0
}



//corpo de metodo
assert test2 {
#LiteralValue >= 0
}

//visibilidade
assert test3 {
all m : Method | m.acc = public || m.acc = protected || m.acc = private_ || #m.acc = 0
}

//ids
assert test4 {
all mid:MethodId | some m:Method | m.id = mid
all cid:ClassId | some c:Class | c.id = cid
all id1:FieldId | some f:Field | f.id = id1
}

assert test5 {
no c1,c2: Class | c1 != c2 && c1.package = c2.package && c1.id = c2.id
}

check test1 for 7
check test2 for 7
check test3 for 7
check test4 for 7
check test5 for 7



