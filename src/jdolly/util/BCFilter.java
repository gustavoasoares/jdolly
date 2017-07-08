package jdolly.util;
import java.io.File;

/**
 * This class represents a filter of behavioral change.
 * */
public class BCFilter {
	
	public static void main(String[] args) {
		run();
	}

	private static void run() {
		String path = "/Users/gustavo/Doutorado/experiments/refactoring-constraints-new/encapsulatefield/last/";
		File[] tests = Util.getTestsFrom(path);

		printTheTotalOfBehavioralChangesIn(tests);
	}

	private static void printTheTotalOfBehavioralChangesIn(File[] tests) {
		int totalOfBehavioralChange = getTotalOfBehavioralChange(tests);
		System.out.println("Total of behavioral changes:" + totalOfBehavioralChange);
	}

	private static int getTotalOfBehavioralChange(File[] tests) {
		int totalOfBehaviorChanges = 0;
		
		for (File test : tests) {
			File in = new File(test, "in");
			File out = new File(test, "out/jrrt");
			File bcSR1 = getBCFailureDir(out);
			
			if (bcSR1.exists()) {			
				String program = Util.getAllClassesNames(in);
				printFailedTest(test);
				Util.printPrograms(in, out); // print the programs from input and output dir
				totalOfBehaviorChanges++;	
			}
		}
		return totalOfBehaviorChanges;
	}

	private static File getBCFailureDir(File out) {
		File bcSR1 = new File(out, "BEHAVIORCHANGE_FAILURE");
		return bcSR1;
	}

	private static void printFailedTest(File test) {
		System.out.println(test);
	}
}
