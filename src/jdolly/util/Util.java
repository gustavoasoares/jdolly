package jdolly.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class Util {
	public static void printPrograms(File in, File out) {
		System.out.println("-----------------------------------------");
		System.out.println("Source");
		System.out.println(getProgram(in));
		System.out.println("-----------------------------------------");
		System.out.println("Target");
		System.out.println(getProgram(out));
	}
	

	public static String getProgram(File path) {
		String result = "";
		
		File[] packages = path.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});

		for (File p : packages) {
			File[] classes = p.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return pathname.getName().endsWith(".java");
				}
			});
			for (File c : classes) {
				result = result + getClass(c) + "\n";
			}
		}
		return result;
	}

	private static String getClass(File c) {
		BufferedReader in;
		String str;
		String result = "";
		try {
			in = new BufferedReader(new FileReader(c));
			while ((str = in.readLine()) != null) {
				result += "\n" + str;
			}
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
