module renamemethod

open javametamodel_nofield_final

run show for 2 Package, 3 Class, 3 Method, 3 ClassId, 3 MethodId, 3 Body


one sig K,N,M extends MethodId {}

fact RenameMethod {

//Main constraint//
  some Method

//Additional constraint//
  differentIdsForClasses[]
  atLeastTwoPackages[]
  differentIdsForMethods[]
  someInheritance[]
  one m:Method | m.id = M && isMain[m]

  someMethodsWithSameNumParameter[]
 

}

pred someMethodsWithSameNumParameter[] {
  one disj m1,m:Method | m1.id = N && m.id = K  && hasSameParameterNumber[m1,m]  
    && isInSameHierarchy[m1,m] && isSimpleValueBody[m] && isSimpleValueBody[m1]
}
pred hasSameParameterNumber[m1,m2:Method] {
m1.param = Int_ && (m2.param = Long_ || m2.param = Int_) 
}

pred isInSameHierarchy[m1,m2:Method] {
  class[m2] in class[m1].extend
}

