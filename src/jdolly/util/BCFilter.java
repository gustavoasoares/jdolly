package jdolly.util;
import java.io.File;

public class BCFilter {
	
	public static void main(String[] args) {
		run();
	}

	private static void run() {
		String path = "/Users/gustavo/Doutorado/experiments/refactoring-constraints-new/encapsulatefield/last/";
		File[] tests = Util.getTestsFrom(path);

		int totalOfBehaviorChangeFails = countHowManyBehaviorChangeFailsExists(tests);
		System.out.println(totalOfBehaviorChangeFails);
	}

	private static int countHowManyBehaviorChangeFailsExists(File[] tests) {
		int totalOfBehaviorChangeFails = 0;
		
		for (File test : tests) {
			File in = new File(test, "in");
			File out = new File(test, "out/jrrt");
			File bcSR1 = new File(out, "BEHAVIORCHANGE_FAILURE");
			
			if (bcSR1.exists()) {			
				String program = Util.getProgramFrom(in);
//				String program2 = Util.getProgram(out);
				//if (program.contains("Class1_0.this.k_0") && !program.contains("int a")  ) {
				System.out.println(test);
				Util.printPrograms(in, out);
				totalOfBehaviorChangeFails++;	
				//}
			}
		}
		return totalOfBehaviorChangeFails;
	}
}
