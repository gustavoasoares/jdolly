package jdolly;

import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.ErrorWarning;
import edu.mit.csail.sdg.alloy4compiler.ast.Command;
import edu.mit.csail.sdg.alloy4compiler.ast.Module;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Options;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod;
import edu.mit.csail.sdg.alloy4viz.VizGUI;
import edu.mit.csail.sdg.alloy4whole.ExampleUsingTheCompiler;
import jdolly.util.Util;

public final class AlloyRunJava {

	public static void main(String[] args) throws Err {

		final String theory = "alloyTheory/pullupmethodwithpkg.als";
		
		run(theory);
	}

	public static long run(final String theory) {		
		A4Reporter report = new A4Reporter() {
			@Override
			public void warning(ErrorWarning msg) {
				System.out.print("Relevance Warning:\n"
						+ (msg.toString().trim()) + "\n\n");
				System.out.flush();
			}
		};

		Util.printTheory(theory);
				
		int currentGeneration = 0;
		Module world;
		try {
			world = CompUtil.parseEverything_fromFile(report, null, theory);
			
			A4Options options = Util.defHowExecCommands();

			for (Command currentCommand : world.getAllCommands()) {
				Util.printCommand(currentCommand);
				A4Solution ans = TranslateAlloyToKodkod.execute_command(report,
						world.getAllReachableSigs(), currentCommand, options);

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
				printTotalOfSoluctions(totalOfSatisfiableAnswers, timeBefore, timeAfter);
			}
		} catch (Err e) {
			e.printStackTrace();
		}
		return currentGeneration;
	}

	private static void printGeneration(final int currentGeneration) {
		System.out.println("Generation: " + currentGeneration);
	}

	private static void printTotalOfSoluctions(final int cont, final long timeBefore, final long timeAfter) {
		System.out.println("The number of soluctions is: " + cont + " in "
				+ (timeAfter - timeBefore) / 1000 + " seconds");
	}
}
