package jdolly.test;

import java.util.List;

import jdolly.JDolly;

import org.eclipse.jdt.core.dom.CompilationUnit;


public class TestUtil {

	
	public static int countSolutions(JDolly jdolly, boolean print) {
		
		int counter = 0;
		for (List<CompilationUnit> cus : jdolly) {
			if (print) {
				for (CompilationUnit compilationUnit : cus) {
					System.out.println(compilationUnit);
				}
			}
			counter++;
		}
		
		return counter;
	}
}
