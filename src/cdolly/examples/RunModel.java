package cdolly.examples;

import java.util.Date;

import cdolly.executor.Executor;

public class RunModel {

	public static void main(String[] args) {
		int a = 2;
		
		
		System.out.println("a");
		
		String alloyModelPath = "/Users/melmongiovi/Documents/doutorado/ExperimentoC/workspace/cdolly-v2/alloy/new/rename_function.als";
		long programCount = 1;
		long skip = 500;
//		String alloyModelPath = args[0];
//		long programCount = Long.parseLong(args[1]);
//		long skip = Long.parseLong(args[2]);
		
		Executor executor = Executor.createExecutor(alloyModelPath, programCount, skip);
		System.out.println("Iniciou...");
		long initialTime = new Date().getTime();
		executor.run();
		long finalTime = new Date().getTime();
		System.out.println("Finalizou. Tempo total: " + (finalTime - initialTime));
	}

}
