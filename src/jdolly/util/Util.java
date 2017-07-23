package jdolly.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import edu.mit.csail.sdg.alloy4compiler.ast.Command;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Options;


public class Util {
	
	public static void printSeparator() {
		System.out.println("----------------");
	}
	
	/** Print all classes names for the given input, which is related to the 
	 * source program (i.e. before applying the transformation) and 
	 * all classes names for the given output, which is related to the target 
	 * program (i.e. after applying the transformation). */
	public static void printPrograms(final File input, final File output) {
		printSource(input);
		printTarget(output);
	}

	private static void printTarget(final File program) {
		final File targetProgram = program;
		System.out.println("-----------------------------------------");
		System.out.println("Target");
		printAllClassesNamesOf(targetProgram);
	}
	
	private static void printSource(final File program) {
		final File sourceProgram = program; 
		System.out.println("-----------------------------------------");
		System.out.println("Source");
		printAllClassesNamesOf(sourceProgram);
	}

	private static void printAllClassesNamesOf(final File input) {
		System.out.println(getAllClassesNames(input));
	}	

	/** 
	 * For the given path, get all packages. After that,
	 * get all classes names(i.e. all the identifiers) from each package and 
	 * join them properly(break line between each name) as a String.
	 * */
	public static String getAllClassesNames(final File path) {
		
		final File[] packages = getPackagesFrom(path);

		final String result = getAllClassesIdentifiersFrom(packages);
		
		return result;
	}

	private static File[] getPackagesFrom(final File path) {
		return path.listFiles(new FileFilter() {
			@Override
			public boolean accept(final File pathname) {
				return pathname.isDirectory();
			}
		});
	}

	private static String getAllClassesIdentifiersFrom(final File[] packages) {
		
		/*StringBuilder is more coherent to be used because 
		 * the entity below will change its value.*/ 
		StringBuilder result = new StringBuilder(StrUtil.EMPTY_STRING);
		File[] classes;
		
		for (final File pack : packages) {
			classes = pack.listFiles(new FileFilter() {
				@Override
				public boolean accept(final File pathname) {
					final String pathName = pathname.getName();
					return pathName.endsWith(StrUtil.JAVA_EXTENSION);
				}
			});
			for (final File class_ : classes) {
				result.append(getClass(class_));
				result.append(StrUtil.BREAK_LINE);
			}
		}
		return result.toString();
	}

	private static String getClass(final File file) {
		String str;
		final StringBuilder result = new StringBuilder(StrUtil.EMPTY_STRING);
		BufferedReader input;
		try {
			input = new BufferedReader(new FileReader(file));
			while ((str = input.readLine()) != null) {
				result.append(StrUtil.BREAK_LINE);
				result.append(str);
			}
			input.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	/* This method can be used in BCReport, BCFilter, 
	 * TSPFilter, NetbeansCounter, Compile e MoveResults. 
	 * Putting him may promote reuse.*/
	public static File[] getTestsFrom(final String path){
		final File refactoring = new File(path);
		return getTestsFromFile(refactoring);
	}
	
	private static File[] getTestsFromFile(final File refactoring) {
		final File[] tests = refactoring.listFiles(new FileFilter() {
			@Override
			public boolean accept(final File pathname) {
				boolean answer = false;
				String nameOfPath = pathname.getName();
				if (nameOfPath.startsWith(StrUtil.TEST_PREFIX))
					answer = true;
				return answer;
			}
		});
		return tests;
	}
	
	/** Use SAT4J, which is the default options for 
	 * SAT Solver, to execute the commands.
	*/
	public static A4Options defHowExecCommands() {
		A4Options options = new A4Options();
		options.solver = A4Options.SatSolver.SAT4J;
		return options;
	}
	
	/** Print a human-readable summary for the given 
	 * Run or Check command that is executed with 
	 * certain scope of Java Language.
	 * For example: === Command Run show for 2 Package, 3 Class, 
	 * 3 ClassId, 1 Method, 1 MethodId, 1 Body, 2 Field, 2 FieldId: ===*/
	public static void printCommand(final Command commandToPrint) {
		System.out.println("=== Command " + commandToPrint
				+ ": ===");
	}
	
	/**This method print each problem and his amount. 
	 * If you want to print the separator 
	 * between each problem, use "Yes" as value for 
	 * hasSeparator parameter. "No", otherwise. */
	public static void printEachProblem(final Map<String, Integer> problems, final String hasSeparator) {
		for (final Map.Entry<String, Integer> problem : problems.entrySet()) {
			if ("Yes".equals(hasSeparator)) {
				System.out.println(StrUtil.SEPARATOR);
			}
			System.out.println(problem.getKey() + ": " + problem.getValue());
		}
	}
	/**Print the given theory. This theory is related to a 
	 * alloy file which formally specifies 
	 * certain constraint related to Java Language scope. 
	 * For example: printTheory("alloyTheory/encapsulatefield_final.als") 
	 * will output: 
	 * === Parsing+Typechecking alloyTheory/encapsulatefield_final.als ===*/
	public static void printTheory(final String theory) {
		System.out.println("=== Parsing+Typechecking " + theory
				+ " ===");
	}
	
}
