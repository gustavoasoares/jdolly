package jdolly.util;

import java.io.File;
import java.io.FileFilter;

public class NetbeansCounter {

	public static void main(String[] args) {
		generateNetbeansExecutionRates();
	}

	private static void generateNetbeansExecutionRates() {
		String path = "/Users/gustavo/Doutorado/experiments/refactoring-constraints-new/renamefield/last";
		File[] tests = Util.getTestsFrom(path);
		int totalOfProgNotRefactByNetbeans = calcTotalOfProgNotRefactByNetbeans(tests);
		
		printNetbeansExecutionRates(tests, totalOfProgNotRefactByNetbeans);
	}

	private static void printNetbeansExecutionRates(File[] tests, int totalOfProgNotRefactByNetbeans) {
		printTotalOfPrograms(tests.length);
		
		printTotalOfProgNotRefactByNetbeans(totalOfProgNotRefactByNetbeans);
	}

	private static int calcTotalOfProgNotRefactByNetbeans(File[] tests) {
		int totalOfProgNotRefactByNetbeans = 0;
		
		for (File test : tests) {
			File outNetBeans = new File(test,"out/netbeans");
			if ( !outNetBeans.exists()){
				totalOfProgNotRefactByNetbeans++;
			}
		}
		return totalOfProgNotRefactByNetbeans;
	}

	private static void printTotalOfProgNotRefactByNetbeans(int count) {
		System.out.println("programs não refatorados pelo netbeans: " + count);
	}

	private static void printTotalOfPrograms(int i) {
		System.out.println("total de programas: " + i);
	}
}
