open core

sig Dummy {}
fact { no Dummy }

pred show {
	#TranslationUnit=1
}

run show
