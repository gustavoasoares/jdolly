package cdolly;

import cdolly.executor.Executor;

public class Main {
	
	static String theoryFile = "";
	static String skip = "1";
	static String programCount = "1";
	
	public static void parseArguments(String[] args) {
		String arg;
		int i = 0;

		while (i < args.length && args[i].startsWith("-")) {
			arg = args[i++];

			if (arg.equals("-addconstraints")) {
				if (i < args.length)
					theoryFile = args[i++];
				else
					System.err.println("-addconstraints requires a path");
				
			} else if (arg.equals("-skip")) {
				if (i < args.length)
					skip = args[i++];
				else
					System.err.println("-skip requires a skip number");
			} 
		}
	}
	
	public static void run() {
		int programc = Integer.parseInt(programCount);
		int sk = Integer.parseInt(skip);
		Executor executor = Executor.createExecutor(theoryFile, programc, sk);
		executor.run();
	}
}
