package jdolly.examples;

import java.util.List;

import jdolly.JDolly;
import jdolly.JDollyImp;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.testorrery.IGenerator;


public class PullUpMethodGen extends Gen{

	@Override
	protected IGenerator<List<CompilationUnit>> getCUGen() {
		JDolly jdolly = new JDollyImp("alloyTheory/pullupmethod_final.als", 2, 3, 4);
		return jdolly;
	}

	@Override
	protected String getLogDirPath() {
		return super.getSystemTempDir() + "/pullupmethod";
	}

}
