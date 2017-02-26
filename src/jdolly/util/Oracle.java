package jdolly.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import oracles.IOracle;
import oracles.OracleFactory;

public class Oracle {

	private static IOracle _eclipseOracle = OracleFactory.createOracleFor("eclipse");
	private static IOracle _jrrtOracle = OracleFactory.createOracleFor("jrrt");
	
	public static void behaviorchanges(String resultFolderPath) {
		File resultFolder = new File(resultFolderPath);
		File[] listFiles = resultFolder.listFiles();
		for (File test : listFiles) {
			if (test.isDirectory()) {
				File[] testFolders = test.listFiles();
				for (File testFolder : testFolders) {
					if (testFolder.getName().equals("out")) {
						File[] resultsFiles = testFolder.listFiles();
						for (File file : resultsFiles) {
							if (file.getName().equals("BEHAVIORCHANGE_FAILURE")) {
								try {
									String pathname = resultFolderPath
											+ "/behaviorchange/"
											+ test.getName();
									copyDirectory(test, new File(pathname));
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}

				}
			}
		}

	}

	public static void copyDirectory(File sourceLocation, File targetLocation)
			throws IOException {

		if (sourceLocation.isDirectory()) {
			if (!targetLocation.exists()) {
				boolean mkdir = targetLocation.mkdir();
				if (mkdir)
					System.out.println("copiado com sucesso");
				else
					System.out.println("problema ao copiar");
				
			}

			String[] children = sourceLocation.list();
			for (int i = 0; i < children.length; i++) {
				copyDirectory(new File(sourceLocation, children[i]), new File(
						targetLocation, children[i]));
			}
		} else {
			// targetLocation.createNewFile();

			InputStream in = new FileInputStream(sourceLocation);

			OutputStream out = new FileOutputStream(targetLocation);

			// Copy the bits from instream to outstream
			FileUtil.copyFileBitsFromInputStreamToOutputStream(in, out);
			in.close();
			out.close();
		}
	}
	
	/** Group together the failing tests related to the same bug */
	public static void categorize(String engine, String resultFolderPath,
			String problemFileName) {

		Map<String, Integer> problems = new HashMap<String, Integer>();

		File resultFolder = new File(resultFolderPath);
		File[] listFiles = resultFolder.listFiles();
		for (File test : listFiles) {
			if (test.isDirectory()) {
				File out = new File(test, "out");
				File outEngine = new File(out, engine);

				File target = new File(outEngine, problemFileName);

				if (target.exists()) {
					IOracle oracle = OracleFactory.createOracleFor(engine);
					oracle.evaluateCorrectness(problems, test, target);						
				}
				
			}
		}

		for (Map.Entry<String, Integer> problem : problems.entrySet()) {
			System.out.println(problem.getKey() + ": " + problem.getValue());
		}
		System.out.println("Total de bugs: " + problems.size());

	}
	
	public static void categorizeAntigo(String resultFolderPath,
			String problemFileName) {

		Map<String, Integer> problems = new HashMap<String, Integer>();

		File resultFolder = new File(resultFolderPath);
		File[] listFiles = resultFolder.listFiles();
		for (File test : listFiles) {
			if (test.isDirectory()) {
				File out = new File(test, "out");
				File target = new File(out, problemFileName);

				if (target.exists()) {
					_eclipseOracle.evaluateCorrectness(problems, test, target);	
				}
			}
		}

		for (Map.Entry<String, Integer> problem : problems.entrySet()) {
			System.out.println(problem.getKey() + ": " + problem.getValue());
		}
		System.out.println("Total de bugs: " + problems.size());

	}
	
	public static void categorizeOege(String resultFolderPath) {

		Map<String, Integer> problems = new HashMap<String, Integer>();

		File resultFolder = new File(resultFolderPath);
		File[] listFiles = resultFolder.listFiles();
		for (File test : listFiles) {
			if (test.isDirectory()) {
				File[] testFolders = test.listFiles();
				for (File testFolder : testFolders) {
					if (testFolder.getName().equals("COMPILATION_ERROR")) {
						_jrrtOracle.evaluateCorrectness(problems, testFolder, testFolder);
					}

				}

			}
		}
		for (Map.Entry<String, Integer> problem : problems.entrySet()) {
			System.out.println("===========================================");
			System.out.println(problem.getKey() + ": " + problem.getValue());
		}

	}

	public static void main(String[] args) {

//		 Oracle.categorizeOege("/Users/gustavo/Doutorado/experiments/refactoring-constraints/renamefield/jrrt/");
		// Oracle.behaviorchanges("/Users/gustavo/Mestrado/genExperimentos/genExperimento/pullupmethod/package/");
		Oracle
				.categorize(
						"jrrt",
						"/Users/gustavo/Doutorado/experiments/refactoring-constraints-new/movemethod/last/",
						"POST_REFACTOR_NOT_COMPILE");
		
//		Oracle
//		.categorizeAntigo(				
//				"/Users/gustavo/Doutorado/experiments/refactoring-constraints/addparameter/eclipse/",
//				"POST_REFACTOR_NOT_COMPILE");
	}

}
