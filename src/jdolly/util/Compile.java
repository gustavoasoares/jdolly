package jdolly.util;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.PrintWriter;

public class Compile {

	public static String compile(String path) {
		
		ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
		ByteArrayOutputStream outputByteArrayError = new ByteArrayOutputStream();

		PrintWriter pw = new PrintWriter(outputByteArray);
		PrintWriter pwerror = new PrintWriter(outputByteArrayError);

		org.eclipse.jdt.internal.compiler.batch.Main.compile(
				"-classpath rt.jar " + path, pw, pwerror);

		// System.out.println(out.toString());
		// System.out.println(outError.toString());
		return outputByteArrayError.toString();
	}

	public static void main(String[] args) {
		generateCompileRates();
	}

	private static void generateCompileRates() {
//		String path = "/private/var/folders/bx/bxpvKGfwF-yg3RjPZ5LRJk+++TI/-Tmp-/pullupfield0";
		String path = "/Users/gustavo/Doutorado/experiments/refactoring-constraints-new/pullupfield/last";
		
		File[] tests = Util.getTestsFromPath(path);
		
		int totalOfCompilationErrors = countTotalOfCompilationErrors(tests);
		
		int totalOfPrograms = tests.length;
		
		printCompileRates(totalOfCompilationErrors, totalOfPrograms);
	}

	private static void printCompileRates(int totalOfCompilationErrors,	int totalOfPrograms) {
		
		printTotalOfGeneratedPrograms(totalOfPrograms);
		
		printTotalOfCompilationErrors(totalOfCompilationErrors);
		
		printThePercentageOfCompilationErrors(totalOfCompilationErrors, totalOfPrograms);
	}

	private static int countTotalOfCompilationErrors(File[] tests) {
		int totalOfCompilationErrors = 0;
		
		for (File test : tests) {
			File out = new File(test,"in");
			String outputMsg = compile(out.toString());
            
            if (outputMsg.contains("ERROR in")) {
            	totalOfCompilationErrors++;        	
            }
		}
		return totalOfCompilationErrors;
	}

	private static void printThePercentageOfCompilationErrors(int i, int j) {
		double per = (i * 100) / j; 
		System.out.println("porcentagem:" + per);
	}

	private static void printTotalOfCompilationErrors(int i) {
		System.out.println("nœmero de erros de compila�‹o: " + i);
	}

	private static void printTotalOfGeneratedPrograms(int j) {
		System.out.println("total de programas gerados: " + j);
	}

}
