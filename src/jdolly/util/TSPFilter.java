package jdolly.util;

import java.io.File;
import java.util.Enumeration;
import java.util.List;

public class TSPFilter {

	public static void main(String[] args) {
		run();
	}

	private static void run() {
		String path = "/Users/gustavo/Doutorado/experiments/refactoring-constraints-new/"
				+ StrUtil.REFACT_MOVE_METHOD + "/last/";
		File[] tests = Util.getTestsFrom(path);

		for (File test : tests) {
			existsTooStrongPreconditionIn(test);
		}	
		saveFileContentFrom(path);
	}

	/** Check if Too Strong Precondition exists in the given file. 
	 * Too Strong Precondition means the precondition is 
	 * very restrictive and complex to satisfy. As a result, it is difficult to guarantee behavior 
	 * preservation.*/
	private static void existsTooStrongPreconditionIn(File test) {
		File in = new File(test, "in");
		File out = new File(test, "out/" + StrUtil.JRRT_REFACTORING_TOOLS);
		File tooStrongPrecondition = new File(out, StrUtil.TOO_STRONG);
		
		if (tooStrongPrecondition.exists()) {
			File messageFile = new File(out, StrUtil.REFACT_INAPPLICABLE);
			String message = FileUtil.leArquivo(messageFile.getAbsolutePath());		
			message = getMessageTemplate(message);
			String program = Util.getAllClassesNames(in);
			program = test.getAbsolutePath() + "\n\n" + program;

			Precondition precondValues = Precondition.getNamed(message);
			
			if (precondValues != null) {
				addProgToGivenPrecond(program, precondValues);					
			} else
				Precondition.loadPrecondition(message, program);
		}
	}

	private static void addProgToGivenPrecond(String program, Precondition precondValues) {
		List<String> programsFromPrecondition = precondValues.getPrograms();
		programsFromPrecondition.add(program);
		precondValues.setQnt(precondValues.getQnt() + 1);
	}

	private static void saveFileContentFrom(String path) {
		int i = 1;
		Enumeration pres = Precondition.getAll().elements();		
		while (pres.hasMoreElements()) {
			Precondition currentPrecondition = (Precondition) pres.nextElement();
			printPreconditionDetails(currentPrecondition);
			
			//SALVA PROGRAMAS
			String file = path + StrUtil.JRRT_REFACTORING_TOOLS + "_PRE" + i;
			String content = getProgramsContentFrom(currentPrecondition);
			FileUtil.gravaArquivo(file, content);

			Util.printSeparator();
			
			i++;
		}
	}

	private static void printPreconditionDetails(Precondition currentPrecondition) {
		System.out.println(currentPrecondition.getMessage());
		System.out.println(currentPrecondition.getQnt());
	}

	private static String getProgramsContentFrom(Precondition currentPrecondition) {
		String content = currentPrecondition.getMessage() + "\n";
		List<String> programs = currentPrecondition.getPrograms();
		int currentProgram = 1;
		for (String program : programs) {
			content = content + "---------------\n";
			content = content + "Program " + currentProgram + "\n";
			content = content + program;
			
			currentProgram++;
		}
		return content;
	}

	private static String getMessageTemplate(String message) {
		String result = message.replaceAll("'\\S*", "'[]'");
		result = result.replaceAll("'\\n.*", "'");
		return result;
	}

}
