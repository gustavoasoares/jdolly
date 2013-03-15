module encapsulate_field

open javametamodel_withfield_nofieldcall


run show for 2 Package, 3 Class, 1 Field, 3 Method, 3 ClassId, 3 MethodId, 1 FieldId, 3 Body

one sig getfieldid, caller extends MethodId {}

fact Encapsulate {

//geral
some Field

somePublicField[] 

differentIdsForClasses[]
differentIdsForMethods[]
#Package = 2
someTester[caller]
someTester[getfieldid] 
someGetter[]
}

pred someGetter[] {
  one m:Method | m.b in LiteralValue && #m.param = 0 && m.id = getfieldid 
}

pred somePublicField[] {
  some f:Field| f.acc = public && f.type = Long_ 
}




