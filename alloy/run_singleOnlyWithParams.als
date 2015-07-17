open core

sig Dummy {}
fact { no Dummy }

pred show {
	#TranslationUnit=1 #Function.param>0
}
run show
