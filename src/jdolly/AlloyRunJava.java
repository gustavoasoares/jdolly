package jdolly;

import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.ConstList;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4compiler.ast.Command;
import edu.mit.csail.sdg.alloy4compiler.ast.Module;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Options;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod;
import jdolly.util.TimeInterval;
import jdolly.util.Util;

public final class AlloyRunJava {

	public static void main(String[] args) throws Err {
		final String theory = "alloyTheory/pullupmethodwithpkg.als";
		
		run(theory);
	}

	public static long run(final String theory) {		
		
		Util.printTheory(theory);
		
		int totalOfGeneratedPrograms = 0;
		try {
			totalOfGeneratedPrograms = (int) getTotalGenProgsBy(theory);
		} catch (Err e) {
			e.printStackTrace();
		}
		return totalOfGeneratedPrograms;
	}

	private static long getTotalGenProgsBy(final String theory) throws Err {
		A4Reporter report = JDolly.createA4Reporter();
		A4Options options = Util.defHowExecCommands();
		Module alloyModuleOfTheory = CompUtil.parseEverything_fromFile(report, null, theory);
		ConstList<Command> commandsInModule = alloyModuleOfTheory.getAllCommands();
		int currentGeneration = 0;
		
		for (Command currentCommand : commandsInModule) {
			Util.printCommand(currentCommand);
			A4Solution ans = TranslateAlloyToKodkod.execute_command(report,
					alloyModuleOfTheory.getAllReachableSigs(), currentCommand, options);

			int totalOfSatisfiableAnswers = 1;
			long timeBefore = System.currentTimeMillis();
			
			while (ans != null) {
				printGeneration(currentGeneration);
				ans = ans.next();
				if (ans.satisfiable())
					totalOfSatisfiableAnswers++;
				else
					break;
				currentGeneration++;
			}
			
			long timeAfter = System.currentTimeMillis();
			
			TimeInterval timeInterval = new TimeInterval(timeBefore, timeAfter);
			printTotalOfSoluctions(totalOfSatisfiableAnswers, timeInterval);
		}
		return currentGeneration;
	}

	private static void printGeneration(final int currentGeneration) {
		System.out.println("Generation: " + currentGeneration);
	}

	private static void printTotalOfSoluctions(final int totalOfSoluctions, final TimeInterval timeInterval) {
		System.out.println("The number of soluctions is: " + totalOfSoluctions + " in "
				+ timeInterval.intervalInSecsToStr());
	}
	
}
