package jdolly.util;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**This class is useful to give a report about the existence or absence of 
 *behavioral change. If there are any behavioral change, the report also 
 *displays the set of unsuccessful tests that reveals such behavior violation. 
 * */
public class BCReport {

	private static List<String> safeRefactorAntigo = new ArrayList<String>();
	private static List<String> safeRefactorNovo = new ArrayList<String>();
	
	public static void main(String[] args) {
		generateBehaviorChangeReport();
	}

	/** */
	private static void generateBehaviorChangeReport() {
		String path = "/Users/gustavo/Doutorado/experiments/refactoring-constraints-new/addparameter/last/";
		File[] tests = Util.getTestsFrom(path);
				
		String testRepresentation = StrUtil.EMPTY_STRING;
		
		for (File test : tests) {
			testRepresentation = test.toString();
			
			File in = new File(test, "in");
			File out = new File(test, "out/jrrt");
//			File out2 = new File(test, "out/eclipse2");

			File bcSR1 = new File(out, "BEHAVIORCHANGE_FAILURE");
			File bcSR2 = new File(out, "BEHAVIORCHANGE_FAILURE2");
			
			if (bcSR1.exists() || bcSR2.exists()) {
				System.out.println(testRepresentation);
			}
			boolean onlyBCSR1Exists = (bcSR1.exists() && !bcSR2.exists());
			if (onlyBCSR1Exists) {
				safeRefactorAntigo.add(testRepresentation);
				System.out.println(testRepresentation);
				Util.printPrograms(in, out);
			}
			boolean onlyBCSR2Exists = !bcSR1.exists() && bcSR2.exists();
			if (onlyBCSR2Exists) {
				safeRefactorNovo.add(testRepresentation);
				Util.printPrograms(in, out);
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
