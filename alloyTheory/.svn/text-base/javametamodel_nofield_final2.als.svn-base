module javametamodel_nofield

// ABSTRACT SYNTAX

abstract sig Id {}

sig Package{} 

sig ClassId, MethodId extends Id {}

abstract sig Accessibility {}

one sig public, private_, protected extends Accessibility {}

abstract sig Type {}

abstract sig PrimitiveType extends Type {}

one sig Int_, Long_ extends PrimitiveType {}

sig Class extends Type {
	package: one Package,
	id: one ClassId,
	extend: lone Class,
	methods: set Method
} 

fun classes[pack:Package]: set Class {
	pack.~package
}

sig Method {
	id : one MethodId,
    param: lone Type,
    acc: lone Accessibility,
    return: one Type, 
    b: one Body
} 

abstract sig Body {}

sig LiteralValue extends Body {
///valor : Int
} // returns a random value

abstract sig Qualifier {}

one sig this_, super_, qthis_ extends Qualifier {}

//        return k();
//        return this.k();
//        return super.k();
//        return A.this.k(); 

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



// WELL-FORMED RULES
fact JavaWellFormedRules {
	noPackageContainsTwoClassesWithSameId[]
	
    noClassExtendsItself[]

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

pred noClassContainsTwoMethodsWithSameSignature[] {
	all c: Class | all m1,m2:c.methods | m1!=m2 =>(m1.id != m2.id or m1.param != m2.param)
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

//one body per method 
no m1,m2:Method | m1!= m2 && m1.b = m2.b
}

//THE FOLLOWING PREDICATES ARE USED TO TEST SOME REFACTORING IMPLEMENTATIONS
pred someOverriding[] {
  one disj m1,m :Method | m1.id = m.id && #m1.param  = 0 && #m.param = 0 && m1.b + m.b in LiteralValue && sameHierarchy[m1,m]
}

pred someMain[] {
  one m: Method | m.acc = public  && #m.param = 0 && m.b !in LiteralValue 
}

pred someMain[id1: MethodId] {
  one m: Method | m.acc = public  && #m.param = 0 && m.b in MethodInvocation && #m.b.q = 0  && m.b.id = id1  && methodNotCalled[m] 
}

pred isMain[m:Method] {
  m.acc = public  && #m.param = 0 && m.b !in LiteralValue 
}

pred isSimpleValueBody[m:Method] {
   m.b in LiteralValue
}

pred someOverloading[i1:Int,i2:Int] {
some disj m1,m2:Method | m1.id = m2.id && #m1.param = i1 && #m2.param = i2 && m1.param != m2.param && m1.param + m2.param !in Class && m1.b + m2.b in LiteralValue && sameHierarchy[m1,m2]
}

pred sameHierarchy[m1,m2:Method] {
   some c1,c2 : Class | m1 + m2 in c1.methods || (m1 in c1.methods && c1 in c2.^extend && m2 in c2.methods)
}

pred someInheritance[]  {
  some disj c1,c2:Class | c1 in c2.extend
}
pred isCaller[m:Method] {
  m.acc = public  && #m.param = 0 && m.b !in LiteralValue 
}

pred returnMethodCall[m:Method] {
m.b in MethodInvocation || m.b in ConstructorMethodInvocation
}

pred methodNotCalled[m1:Method] {
no mi:MethodInvocation| mi.id = m1.id 
no cmi:ConstructorMethodInvocation | cmi.idMethod = m1.id
}


//AUXILIAR FUNCTIONS
fun class[m:Method]: one Class {
	m.~methods
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

#MethodInvocation >= 0 
all b:MethodInvocation | one mid:MethodId | b.id = mid

#ConstructorMethodInvocation >= 0
all b:ConstructorMethodInvocation  | one mid:MethodId, cid:ClassId | b.idMethod = mid && b.idClass = cid

all m:Method | m.b in LiteralValue || m.b in MethodInvocation || m.b in ConstructorMethodInvocation
}

//visibilidade
assert test3 {
all m : Method | m.acc = public || m.acc = protected || m.acc = private_ || #m.acc = 0
}

//ids
assert test4 {
all mid:MethodId | some m:Method | m.id = mid
all cid:ClassId | some c:Class | c.id = cid
}

assert test5 {
no c1,c2: Class | c1 != c2 && c1.package = c2.package && c1.id = c2.id
}

check test1 for 7
check test2 for 7
check test3 for 7
check test4 for 7
check test5 for 7



