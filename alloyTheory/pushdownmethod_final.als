module pushdownmethod

open javametamodel_nofield_final2

run show for exactly 2 Package, 3 Class, 4 Method,  3 ClassId, exactly 3 MethodId, 4 Body


//geral, nome do metodo refatorado
one sig M extends MethodId {}


fact PushDown {

//push down
some c:Class| someSubClass[c] && someMethod[c] 

//additional constraints
someOverloading[1,1] || someOverriding[]
}


pred someSubClass[c:Class] {
some c2:Class | c in c2.extend
}

pred someMethod[c:Class] {
 some m:Method | m in c.methods && m.id = M
&& isCaller[m] && someMain[m.id]
}








