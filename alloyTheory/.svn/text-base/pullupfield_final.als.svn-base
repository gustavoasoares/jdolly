module pushdownfield

open javametamodel_withfield


run show for  2 Package, 3 Class, 2 Field, 1 Method, 5 Id, 1 Body

fact PullUp {
#Package = 2

some c1,c2:Class, f:Field | c1 in c2.extend && f in c2.fields && c2.id = ID1

differentIdsForClasses[]
someFieldhiding[]
someMain[]
}

one sig ID1 extends ClassId {}




