package jdolly.examples;

import java.util.List;

import jdolly.JDolly;
import jdolly.JDollyImp;
import jdolly.JDollyImp;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.testorrery.IGenerator;


public class MoveMethodGen extends Gen {

	@Override
	protected IGenerator<List<CompilationUnit>> getCUGen() {
		return new JDollyImp("alloyTheory/movemethod_final.als", 2,
				3, 3,1);
	}

	@Override
	protected String getLogDirPath() {
		return super.getSystemTempDir() + "/movemethod";
	}

}
