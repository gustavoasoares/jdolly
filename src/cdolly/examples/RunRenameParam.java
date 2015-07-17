package cdolly.examples;

import cdolly.executor.Executor;

public class RunRenameParam {

	public static void main(String[] args) {
		String alloyModelPath = "alloy/new/rename_macro.als";
		int skip = Integer.parseInt(args[1]);
		Executor executor = Executor.createExecutor(alloyModelPath, Integer.parseInt(args[0]), skip);
		executor.run();
	}

}
