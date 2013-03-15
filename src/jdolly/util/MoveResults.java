package jdolly.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MoveResults {

	public static void main(String[] args) {

		String pathSource = "/Users/gustavo/Doutorado/experiments/refactoring-constraints-new/pullupfieldjrrt/last";
		String pathTarget = "/Users/gustavo/Doutorado/experiments/refactoring-constraints-new/pullupfield/last";

		File refactoring = new File(pathSource);
		File[] tests = refactoring.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (pathname.getName().startsWith("test"))
					return true;
				else
					return false;
			}

		});

		int count = 0;
		int i = 0;

		for (File test : tests) {
			
			File source = new File(test,"out/jrrt");
			File targetDir = new File(pathTarget);
			File targetTestDir = new File(targetDir,test.getName());
			File target = new File(targetTestDir,"out/jrrt");
			
			try {
				copyDirectory(source, target);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			count++;

		}
		System.out
				.println("programas copiados: " + count);
	}

	public static void copyDirectory(File sourceLocation, File targetLocation)
			throws IOException {

		if (sourceLocation.isDirectory()) {
			if (!targetLocation.exists()) {
				targetLocation.mkdir();
			}

			String[] children = sourceLocation.list();
			for (int i = 0; i < children.length; i++) {
				copyDirectory(new File(sourceLocation, children[i]), new File(
						targetLocation, children[i]));
			}
		} else {

			InputStream in = new FileInputStream(sourceLocation);
			OutputStream out = new FileOutputStream(targetLocation);

			// Copy the bits from instream to outstream
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		}

	}
}
