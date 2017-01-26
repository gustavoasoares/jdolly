package jdolly.util;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class BCReport {

	private static List<String> safeRefactorAntigo = new ArrayList<String>();
	private static List<String> safeRefactorNovo = new ArrayList<String>();
	
	public static void main(String[] args) {
		run();
	}

	private static void run() {
		String path = "/Users/gustavo/Doutorado/experiments/refactoring-constraints-new/addparameter/last/";
		File[] tests = Util.getTestsFrom(path);

		int totalOfbcSR1Individually = 0;
		int totalOfbcSR2Individually = 0;
				
		String stringRepresentationOfTest = StrUtil.EMPTY_STRING;
		
		for (File test : tests) {
			stringRepresentationOfTest = test.toString();
			
			File in = new File(test, "in");
			File out = new File(test, "out/jrrt");
//			File out2 = new File(test, "out/eclipse2");

			File bcSR1 = new File(out, "BEHAVIORCHANGE_FAILURE");
			File bcSR2 = new File(out, "BEHAVIORCHANGE_FAILURE2");
			
			if (bcSR1.exists() || bcSR2.exists()) {
				System.out.println(stringRepresentationOfTest);
			}
			boolean onlyBCSR1Exists = (bcSR1.exists() && !bcSR2.exists());
			if (onlyBCSR1Exists) {
				safeRefactorAntigo.add(stringRepresentationOfTest);
				System.out.println(stringRepresentationOfTest);
				Util.printPrograms(in, out);
				totalOfbcSR1Individually++;
			}
			boolean onlyBCSR2Exists = !bcSR1.exists() && bcSR2.exists();
			if (onlyBCSR2Exists) {
				safeRefactorNovo.add(stringRepresentationOfTest);
				Util.printPrograms(in, out);
				totalOfbcSR2Individually++;
			}
		}
		printSafeRefactExecution("V0","V1");
		printSafeRefacTests(safeRefactorAntigo);
		
		printSafeRefactExecution("V1","V0");
		printSafeRefacTests(safeRefactorNovo);
	}

	private static void printSafeRefacTests(List<String> safeRefactorAntigo) {
		for (String string : safeRefactorAntigo) {
			System.out.println(string);
		}
	}

	private static void printSafeRefactExecution(String safeRefactWorked, String safeRefactNotWorked) {
		System.out.println("SafeRefactor " + safeRefactWorked + 
				" pegou mas SafeRefactor " + 
				safeRefactNotWorked + " não pegou");
	}


	

}
