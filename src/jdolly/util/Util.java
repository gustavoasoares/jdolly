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
	
	/* The modifier 'final' brings more consistency to the objects, 
	 * because they will not change their values.*/
	public static void printPrograms(final File input, final File output) {
		printSource(input);
		printTarget(output);
	}

	private static void printTarget(final File output) {
		System.out.println("-----------------------------------------");
		System.out.println("Target");
		System.out.println(getProgramsFrom(output));
	}

	public static void printSeparator() {
		System.out.println("----------------");
	}
	
	private static void printSource(final File input) {
		System.out.println("-----------------------------------------");
		System.out.println("Source");
		System.out.println(getProgramsFrom(input));
	}	

	public static String getProgramsFrom(File path) {
		
		File[] packages = getPackagesFrom(path);

		String result = getClassesNamesFrom(packages);
		
		return result;
	}

	private static File[] getPackagesFrom(File path) {
		return path.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
	}

	private static String getClassesNamesFrom(File[] packages) {
		
		//StringBuilder is more coherent to be used because the entity below will change its value. 
		StringBuilder result = new StringBuilder(StrUtil.EMPTY_STRING);
		File[] classes;
		
		for (File pack : packages) {
			classes = pack.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					String nameOfPath = pathname.getName();
					return nameOfPath.endsWith(StrUtil.JAVA_EXTENSION);
				}
			});
			for (File _class : classes) {
				result.append(getClass(_class));
				result.append(StrUtil.BREAK_LINE);
			}
		}
		return result.toString();
	}

	private static String getClass(File file) {
		String str;
		StringBuilder result = new StringBuilder(StrUtil.EMPTY_STRING);
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


	// This method can be used in BCReport, BCFilter, TSPFilter, NetbeansCounter, Compile e MoveResults. 
	// Putting him may promote reuse.
	public static File[] getTestsFromPath(String path){
		File refactoring = new File(path);
		return getTestsFromFile(refactoring);
	}
	
	public static File[] getFilesFromPath(String resultFolderPath) {
		File resultFolder = new File(resultFolderPath);
		File[] listFiles = resultFolder.listFiles();
		return listFiles;
	}
	
	public static A4Options defHowExecCommands() {
		A4Options options = new A4Options();
		options.solver = A4Options.SatSolver.SAT4J;
		return options;
	}
	
	public static void printCurrentCommandExec(final Command command) {
		System.out.println("============ Command " + command
				+ ": ============");
	}
	
	public static File[] getTestsFromFile(File refactoring) {
		File[] tests = refactoring.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				boolean answer = false;
				String nameOfPath = pathname.getName();
				if (nameOfPath.startsWith(StrUtil.TEST_PREFIX))
					answer = true;
				return answer;
			}
		});
		return tests;
	}
	/** If you want to print the separator between each problem, use "Yes" as value for 
	 * hasSeparator parameter. "No", otherwise. */
	public static void printEachProblemAndHisAmountWithOrWithoutSeparator(Map<String, Integer> problems, 
			String hasSeparator) {
		for (Map.Entry<String, Integer> problem : problems.entrySet()) {
			if(hasSeparator == "Yes"){
				System.out.println(StrUtil.SEPARATOR);
			}
			System.out.println(problem.getKey() + ": " + problem.getValue());
		}
	}
	
}
