package jdolly.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MoveResults {

	public static void main(String[] args) {
		run();
	}

	private static void run() {
		
		String pathTarget = "/Users/gustavo/Doutorado/experiments/refactoring-constraints-new/pullupfield/last";
		
		int totalOfCopiedFiles = countHowManyFilesInTargetPathWereCopied(pathTarget);
		
		printTotalOfCopiedFiles(totalOfCopiedFiles);
	}

	private static int countHowManyFilesInTargetPathWereCopied(String pathTarget) {
		String pathSource = "/Users/gustavo/Doutorado/experiments/refactoring-constraints-new/pullupfieldjrrt/last";
		File[] tests = Util.getTestsFrom(pathSource);
		int count = 0;
		for (File test : tests) {
			File source = new File(test,"out/jrrt");
			File targetDir = new File(pathTarget);
			File targetTestDir = new File(targetDir,test.getName());
			File target = new File(targetTestDir,"out/jrrt");
			
			tryToCopyDirectory(source, target);
			
			count++;
		}
		return count;
	}	
	
	private static void tryToCopyDirectory(File source, File target) {
		try {
			copyDirectory(source, target);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	private static void printTotalOfCopiedFiles(int count) {
		System.out
				.println("programas copiados: " + count);
	}

	public static void copyDirectory(File sourceLocation, File targetLocation)
			throws IOException {

		if (sourceLocation.isDirectory()) {
			createDirectoryIfNotExists(targetLocation);

			String[] children = sourceLocation.list();
			for (int i = 0; i < children.length; i++) {
				copyDirectory(new File(sourceLocation, children[i]), new File(
						targetLocation, children[i]));
			}
		} else {
			InputStream in = new FileInputStream(sourceLocation);
			OutputStream out = new FileOutputStream(targetLocation);

			FileUtil.copyFileBitsFromInputStreamToOutputStream(in, out);
			
			in.close();
			out.close();
		}

	}

	private static void createDirectoryIfNotExists(File targetLocation) {
		if (!targetLocation.exists()) {
			targetLocation.mkdir();
		}
	}
}
