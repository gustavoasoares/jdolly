module pullupmethod

open javametamodel_nofield


assert testDuplicatedClassName {
  no c1,c2:Class | c1 != c2 && c1.package = c2.package && c1.id = c2.id
}

check testDuplicatedClassName for 7

