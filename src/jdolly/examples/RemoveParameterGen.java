package jdolly.examples;

import java.util.List;

import jdolly.JDolly;
import jdolly.JDollyImp;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.testorrery.IGenerator;


public class RemoveParameterGen extends Gen {

	@Override
	protected IGenerator<List<CompilationUnit>> getCUGen() {
		return new JDollyImp("newAlloyTheory/removeparameter_idioma_novo.als", 2, 3,
				2);
	}

	@Override
	protected String getLogDirPath() {
		return super.getSystemTempDir() + "/removeparameter";
	}

}
