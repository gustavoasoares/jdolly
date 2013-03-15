module pushdownfield

open javametamodel_withfield_final


run show for  2 Package, 3 Class, 2 Field, 1 Method, 5 Id, 1 Body


one sig A extends ClassId {}

fact PushDown {
#Package = 2

some c1,c2:Class | c1 in c2.extend && some c1.fields && c1.id = A

differentIdsForClasses[]
someFieldhiding[]
someMain[]
}




