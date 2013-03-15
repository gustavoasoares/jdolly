package jdolly.examples;

import java.util.List;

import jdolly.JDolly;
import jdolly.JDollyImp;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.testorrery.IGenerator;



public class AddParameterGen extends Gen {

	@Override
	protected IGenerator<List<CompilationUnit>> getCUGen() {
		return new JDollyImp("alloyTheory/addparameter_final.als", 2, 3,
				3);
	}

	@Override
	protected String getLogDirPath() {
		return super.getSystemTempDir() + "/addparameter";
	}

}
