package jdolly.main;

import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;

import jdolly.examples.TestLogger;

public class ProgramDetailer {
	
	private boolean printPrograms;
	private boolean logFiles; 
	private boolean checkCompilationErrors;
	
	public ProgramDetailer(final boolean printPrograms, final boolean logFiles, final boolean checkCompilationErrors) {
		this.printPrograms = printPrograms;
		this.logFiles = logFiles;
		this.checkCompilationErrors = checkCompilationErrors;
	}

	public boolean shouldPrintPrograms() {
		return printPrograms;
	}

	public void setPrintPrograms(final boolean printPrograms) {
		this.printPrograms = printPrograms;
	}

	public boolean shouldPrintLogFiles() {
		return logFiles;
	}

	public void setLogFiles(final boolean logFiles) {
		this.logFiles = logFiles;
	}

	public boolean shouldCheckForCompilationErrors() {
		return checkCompilationErrors;
	}

	public void setCheckCompilationErrors(final boolean checkCompilationErrors) {
		this.checkCompilationErrors = checkCompilationErrors;
	}
	
	public void printPrograms(List<CompilationUnit> cus){
		for (CompilationUnit compilationUnit : cus) {
			System.out.println(compilationUnit);
		}
	}

	public static void printCompilationErrorsRates(TestLogger logger) {
		printTotalOfCompilationErrors(logger);
		double per = (logger.getCompilererrors() * 100)
				/ logger.getGeneratedCount();
		printPercentageOfCompilationErrors(per);
	}

	private static void printPercentageOfCompilationErrors(double per) {
		System.out.println("percentage of compilation errors:" + per);
	}

	private static void printTotalOfCompilationErrors(TestLogger logger) {
		System.out.println("number of compilation errors: "
				+ logger.getCompilererrors());
	}
	

}
