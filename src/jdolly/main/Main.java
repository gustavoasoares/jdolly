package jdolly.main;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

import jdolly.JDolly;
import jdolly.JDollyFactory;
import jdolly.Scope;
import jdolly.examples.TestLogger;


import org.eclipse.jdt.core.dom.CompilationUnit;

public class Main {

	private static String theoryFile = "alloyTheory/default.als";
	private static String output = "";
	private static JDolly generator;
	private static Scope scope;

	private static Long maxPrograms;

	public static void main(String[] args) {
		parseArguments(args);
		generator = JDollyFactory.getInstance().createJDolly(scope, theoryFile);
		generatePrograms(true, true, false, maxPrograms);

	}

	public static void generatePrograms(boolean printPrograms,
			boolean logFiles, boolean checkCompilationErrors, Long maxPrograms) {

		long count = 0;

		TestLogger logger = new TestLogger(output);

		for (List<CompilationUnit> cus : generator) {

			if (maxPrograms != null && count == maxPrograms)
				break;

			if (printPrograms) {
				for (CompilationUnit compilationUnit : cus) {
					System.out.println(compilationUnit);
				}

			}

			if (logFiles)
				logger.logGenerated(cus, checkCompilationErrors);

			count++;

		}

		if (checkCompilationErrors) {
			System.out.println("número de erros de compilação: "
					+ logger.getCompilererrors());
			double per = (logger.getCompilererrors() * 100)
					/ logger.getGeneratedCount();
			System.out.println("porcentagem de erros de compilaÁ„o:" + per);
		}

	}

	/**
	 * Utility function that returns the system's temporary directory.
	 */
	public static String getSystemTempDir() {
		String tempdir = System.getProperty("java.io.tmpdir");
		if (tempdir == null) {
			throw new IllegalArgumentException("Temp dir is not specified");
		}
		String separator = System.getProperty("file.separator");
		if (!tempdir.endsWith(separator)) {
			return tempdir + separator;
		}
		return tempdir;
	}

	private static void parseArguments(String[] args) {
		boolean vflag = true;
		String arg;
		int i = 0;

		while (i < args.length && args[i].startsWith("-")) {
			arg = args[i++];

			if (arg.equals("-addconstraints")) {
				if (i < args.length)
					theoryFile = args[i++];
				else
					System.err.println("-addconstraints requires a path");
				if (vflag)
					System.out.println("addconstraints path= " + theoryFile);
			} else if (arg.equals("-output")) {
				if (i < args.length)
					output = args[i++];
				else
					System.err.println("-output requires a path");
				if (vflag)
					System.out.println("output path= " + output);
			} else if (arg.equals("-maxprograms")) {
				if (i < args.length)
					maxPrograms = Long.parseLong(args[i++]);
				else
					System.err.println("-maxprograms requires a number");
				if (vflag)
					System.out.println("output path= " + output);
			} else if (arg.equals("-scope")) {
				if (i < args.length) {
					int maxPackage = Integer.parseInt(args[i++]);
					int maxClass = Integer.parseInt(args[i++]);
					int maxMethod = Integer.parseInt(args[i++]);
					int maxField = Integer.parseInt(args[i++]);
					scope = new Scope(maxPackage, maxClass, maxMethod, maxField);
				} else
					System.err
							.println("-scope requires number of packages, classes, methods, and fields");
				if (vflag)
					System.out.println("Scope= " + scope.getMaxPackage() + " "
							+ scope.getMaxClass() + " " + scope.getMaxMethod()
							+ " " + scope.getMaxField());

			}
		}

		// if (i == args.length || i == args.length)
		// System.err
		// .println("Usage: Main [-output path] [-addconstraints path]");

	}

	public static String readInputStreamAsString(InputStream in)
			throws IOException {

		BufferedInputStream bis = new BufferedInputStream(in);
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		int result = bis.read();
		while (result != -1) {
			byte b = (byte) result;
			buf.write(b);
			result = bis.read();
		}
		return buf.toString();
	}
}
