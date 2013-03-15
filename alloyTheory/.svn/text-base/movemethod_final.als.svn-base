module movemethod

open javametamodel_withfield_nofieldcall_final

run show for 2 Package, 3 Class,1 Field, 3 Method, 6 Id, 3 Body

one sig A extends ClassId {}
one sig M extends MethodId {}

fact MoveMethod {   
  overloading[Int_,Long_] || someOverriding[]
  someCaller[]
  #Package = 2
  differentIdsForClasses[]

  some c:Class | c.id = A && someTargetClassField[c] && someMethodToMove[c]



}

pred additionalConstraints[f:Field, m:Method] {
   f.acc = public
  m.b in LiteralValue && m.param = Int_ 
}

pred someTargetClassField[c:Class] {
some f:Field, c2:Class | f in c.fields && f.acc = public && f.type = c2  && c != c2
}

pred someMethodToMove[c:Class] {
some m:Method | m in c.methods &&  m.b in LiteralValue && m.param = Int_ && m.id = M 
} 


