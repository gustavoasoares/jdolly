package jdolly.examples;

import java.util.List;

import jdolly.JDolly;
import jdolly.JDollyImp;
import jdolly.JDollyImp;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.testorrery.IGenerator;


public class RenameFieldGen extends Gen{

	@Override
	protected IGenerator<List<CompilationUnit>> getCUGen() {
		JDolly jdolly = new JDollyImp("alloyTheory/renamefield_final.als", 2, 3, 1,2);
		return jdolly;
	}

	@Override
	protected String getLogDirPath() {
		return super.getSystemTempDir() + "/renamefield";
	}

}
