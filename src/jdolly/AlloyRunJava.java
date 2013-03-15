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

public final class AlloyRunJava {

	public static void main(String[] args) throws Err {

		run("alloyTheory/pullupmethodwithpkg.als");
	}

	public static long run(String theory) {
		VizGUI viz = null;
		int i = 0;
		A4Reporter rep = new A4Reporter() {
			@Override
			public void warning(ErrorWarning msg) {
				System.out.print("Relevance Warning:\n"
						+ (msg.toString().trim()) + "\n\n");
				System.out.flush();
			}
		};

		System.out.println("=========== Parsing+Typechecking " + theory
				+ " =============");
		Module world;
		try {
			world = CompUtil.parseEverything_fromFile(rep, null, theory);
			// Choose some default options for how you want to execute the
			// commands
			A4Options options = new A4Options();
			options.solver = A4Options.SatSolver.SAT4J;

			for (Command command : world.getAllCommands()) {
				// Execute the command
				System.out.println("============ Command " + command
						+ ": ============");
				A4Solution ans = TranslateAlloyToKodkod.execute_command(rep,
						world.getAllReachableSigs(), command, options);
				// Print the outcome
				// System.out.println(ans);
				int cont = 1;
				long tempoAntes = System.currentTimeMillis();

				while (ans != null) {
					 System.out.println("Geracao: " + i);
					ans = ans.next();
					if (ans.satisfiable())
						cont++;

					else
						break;
					i++;
				}
				long tempoDepois = System.currentTimeMillis();
				System.out.println("O número de soluções é: " + cont + " em "
						+ (tempoDepois - tempoAntes) / 1000 + "s");
				// If satisfiable...
				// if (ans.satisfiable()) {
				// // You can query "ans" to find out the values of each set or
				// type.
				// // This can be useful for debugging.
				// //
				// // You can also write the outcome to an XML file
				// ans.writeXML("alloy_example_output.xml");
				// //
				// // You can then visualize the XML file by calling this:
				// if (viz==null) {
				// viz = new VizGUI(false, "alloy_example_output.xml", null);
				// } else {
				// viz.loadXML("alloy_example_output.xml", true);
				// }
				// }
				// }
			}
		} catch (Err e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return i;
	}
}
