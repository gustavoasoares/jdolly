package cdolly.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class CFileTest {

	@Test
	public void test() {
		CFile cFile = new CFile();
		cFile.addDefine(new Define("A"));
		Function f1 = new Function(VariableType.voidd, "f1");
		f1.addParam(new Variable(VariableType.bool, "param1"));
		f1.addParam(new Variable(VariableType.floatt, "param2"));
		f1.addStatement(new VarDeclarationStatement(new Variable(VariableType.charr, " char1"), "'a'"));
		cFile.addFunction(f1);
		cFile.addFunction(new Function(VariableType.intt, "f2"));
		System.out.println(cFile.getCSource());
	}

}
