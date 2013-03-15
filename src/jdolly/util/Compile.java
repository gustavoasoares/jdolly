package jdolly.util;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.ProjectHelper;


public class Compile {

	public static String compile(String path) {
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayOutputStream outError = new ByteArrayOutputStream();

		PrintWriter pw = new PrintWriter(out);
		PrintWriter pwerror = new PrintWriter(outError);

		org.eclipse.jdt.internal.compiler.batch.Main.compile(
				"-classpath rt.jar " + path, pw, pwerror);

		// System.out.println(out.toString());

		// System.out.println(outError.toString());
		return outError.toString();
	}

	public static void main(String[] args) {
//		String path = "/private/var/folders/bx/bxpvKGfwF-yg3RjPZ5LRJk+++TI/-Tmp-/pullupfield0";
		String path = "/Users/gustavo/Doutorado/experiments/refactoring-constraints-new/pullupfield/last";
		File refactoring = new File(path);
		File[] tests = refactoring.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (pathname.getName().startsWith("test"))
					return true;
				else
					return false;
			}

		});
		int i = 0;
		int j = 0;
		for (File test : tests) {
			File out = new File(test,"in");
			String outputMsg = compile(out.toString());
            
            if (outputMsg.contains("ERROR in")) {
            	i++;        	
            }
            j++;
		}
		System.out.println("total de programas gerados: " + j);
		System.out.println("número de erros de compilação: " + i);
		double per = (i * 100) / j; 
		System.out.println("porcentagem:" + per);
		
		

	}

}
