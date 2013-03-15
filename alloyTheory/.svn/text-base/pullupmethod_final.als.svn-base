module pullupmethod

open javametamodel_nofield_final2

run show for exactly 2 Package, 3 Class, 4 Method,  exactly 3 ClassId, exactly 3 MethodId, 4 Body

one sig M extends MethodId{}

fact Pullup {

some c:Class | someSuperClass[c] && someMethod[c] 

someOverloading[1,1] || someOverriding[]

}


//begin: debug
//one sig A1, B1, C1 extends ClassId {}
//one sig A, B, C extends Class {}
//one sig P1, P2 extends Package {}
//fact {
//P1 in A.package
//P2 in B.package
//P1 in C.package
//A in B.extend
//B in C.extend
//one m,m1:Method | m in C.methods && m.id = M && m1 in C.methods && m1 != m && m.b in ConstructorMethodInvocation
//one m1,m2:Method | m1.id = m2.id 
//if I comment the next two lines, Alloy analyzer does not find solutions
// 			&& m1 + m2 in A.methods && m1.acc = protected && m1.param = Int_ 
//                   && m2.param = Long_ && m1.b in LiteralValue && m2.b in LiteralValue
//A.id = A1
//B.id = B1
//C.id = C1
//someMethod[C] 
//}
//end: debug

pred someSuperClass[c:Class] {
   some c2:Class | c2 in c.extend
}

pred someMethod[c:Class] {
 some m:Method | m in c.methods && m.id = M && isCaller[m] && someMain[m.id]
}









