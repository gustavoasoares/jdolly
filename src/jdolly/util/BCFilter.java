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

		int totalOfBehavioralChange = countTotalOfBehavioralChange(tests);
		System.out.println(totalOfBehavioralChange);
	}

	private static int countTotalOfBehavioralChange(File[] tests) {
		int totalOfBehaviorChangeFails = 0;
		
		for (File test : tests) {
			File in = new File(test, "in");
			File out = new File(test, "out/jrrt");
			File bcSR1 = new File(out, "BEHAVIORCHANGE_FAILURE");
			
			if (bcSR1.exists()) {			
				String program = Util.getProgramFrom(in);
				System.out.println(test);
				Util.printPrograms(in, out);
				totalOfBehaviorChangeFails++;	
				//}
			}
		}
		return totalOfBehaviorChangeFails;
	}
}
