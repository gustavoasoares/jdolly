module renameclass

open javametamodel_nofield_final3

run show for 2 Package, 3 Class, 3 Method, 6 Id, 3 Body

fact RenameClass {

//Main constraint//
some Class

//Additional constraint//
atLeastTwoPackages[]
someOverloading[Int_,Long_] || someOverriding[]
differentIdsForClasses[]
someMain[]
someInheritance[]
}



