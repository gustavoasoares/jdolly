module addparameter

open javametamodel_nofield

run show for exactly 2 Package, 3 Class, 3 Method, exactly 3 ClassId, exactly 2 MethodId, 3 Body

one sig M extends MethodId {}

fact AddParameter {

  someOverriding[] || someOverloading[0,1]  
  someCaller[]
  someInheritance[]
}


pred avoidTrivialPreconditions[] {
  no disj m1,m2:Method | m1.id = m2.id && m1.param = Int_ && class[m1] = class[m2]
}

pred someMethodtoAddParameter[] {
 one m1:Method | #m1.param = 0
}

pred someCaller[] {
  one m: Method | m.acc = public  && #m.param = 0 && m.b !in LiteralValue && m.id = M
}


//TESTE
//fact {
//some c1,c2:Class, m1,m2:Method | c1 in c2.extend && m1 in c1.methods && m2 in c2.methods && m1.id = m2.id && #m1.param = 0 && m2.param = Int_
//}
