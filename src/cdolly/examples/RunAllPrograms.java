package cdolly.examples;

import cdolly.executor.Executor;

public class RunAllPrograms {

	public static void main(String[] args) {
		String alloyModelPath = "/Users/gugawag/Documents/mestrado/workspace/cdolly-v2/alloy/new/all_programs.als";
		int programCount = Integer.parseInt(args[0]);
		int skip = Integer.parseInt(args[1]);
		Executor executor = Executor.createExecutor(alloyModelPath, programCount, skip);
		executor.run();
	}

}
