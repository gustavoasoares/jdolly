package cdolly.examples;

import cdolly.executor.Executor;

public class RunExtractFunction {

	public static void main(String[] args) {
		System.out.println("oi");
		String alloyModelPath = "alloy/new/extract_function.als";
		int skip = Integer.parseInt(args[1]);
		Executor executor = Executor.createExecutor(alloyModelPath, Integer.parseInt(args[0]), skip);
		executor.run();
	}

}
