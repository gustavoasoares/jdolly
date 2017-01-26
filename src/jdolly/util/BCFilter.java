package jdolly.util;
import java.io.File;
import java.io.FileFilter;

public class BCFilter {
	public static void main(String[] args) {
		String path = "/Users/gustavo/Doutorado/experiments/refactoring-constraints-new/encapsulatefield/last/";
		File refactoring = new File(path);
		File[] tests = refactoring.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (pathname.getName().startsWith("test"))
					return true;
				else
					return false;
			}

		});

		int count = 0;
		
		
		for (File test : tests) {
			File in = new File(test, "in");
			File out = new File(test, "out/jrrt");

			File bcSR1 = new File(out, "BEHAVIORCHANGE_FAILURE");
			if (bcSR1.exists()) {			
				String program = Util.getProgramsFrom(in);
//				String program2 = Util.getProgram(out);
				//if (program.contains("Class1_0.this.k_0") && !program.contains("int a")  ) {
					System.out.println(test);
					Util.printPrograms(in, out);
					count++;	
				//}
			}
			
			

		}
		System.out.println(count);
	}
}
