package jdolly.util;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class BCReport {

	public static void main(String[] args) {
		String path = "/Users/gustavo/Doutorado/experiments/refactoring-constraints-new/addparameter/last/";
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

		int i = 0;
		int j = 0;
		List<String> safeRefactorAntigo = new ArrayList<String>();
		List<String> safeRefactorNovo = new ArrayList<String>();
		for (File test : tests) {
			File in = new File(test, "in");
			File out = new File(test, "out/jrrt");
//			File out2 = new File(test, "out/eclipse2");

			File bcSR1 = new File(out, "BEHAVIORCHANGE_FAILURE");
			File bcSR2 = new File(out, "BEHAVIORCHANGE_FAILURE2");
			
			if (bcSR1.exists() || bcSR2.exists()) {
				System.out.println(test.toString());
			}
			if ((bcSR1.exists() && !bcSR2.exists())) {
				safeRefactorAntigo.add(test.toString());
				System.out.println(test.toString());
				Util.printPrograms(in, out);
				i++;
			}
			if (!bcSR1.exists() && bcSR2.exists()) {
				safeRefactorNovo.add(test.toString());
				Util.printPrograms(in, out);
				j++;
			}

		}

		System.out
				.println("SafeRefactor V0 pegou mas SafeRefactor V1 não pegou");
		for (String string : safeRefactorAntigo) {
			System.out.println(string);
		}
		System.out
				.println("SafeRefactor V1 pegou mas SafeRefactor V0 não pegou");
		for (String string : safeRefactorNovo) {
			System.out.println(string);
		}

	}

	

}
