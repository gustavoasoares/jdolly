package jdolly.examples;

import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.testorrery.IGenerator;



public abstract class Gen {

	protected static String TEST_DIR = "";
	protected static String FILE_SEPARATOR = System
			.getProperty("file.separator");
	protected static final String TEMP = System.getProperty("java.io.tmpdir");

	protected abstract IGenerator<List<CompilationUnit>> getCUGen();

	protected abstract String getLogDirPath();
	
	public void generatePrograms(boolean printPrograms, boolean logFiles, boolean checkCompilationErrors) {
		
		String refactoringName = this.getClass().getSimpleName().replace("Gen", "");

		TestLogger logger = new TestLogger(refactoringName, getLogDirPath());

		for (List<CompilationUnit> cus : getCUGen()) {
			if (printPrograms) {
				for (CompilationUnit compilationUnit : cus) {
					System.out.println(compilationUnit);
				}
				
			}
			
//			boolean b = false;
//			for (CompilationUnit compilationUnit : cus) {
////				System.out.println(compilationUnit.getPackage());
//				if (compilationUnit.getPackage().toString().contains("package Package_0;")) {
//					String type = compilationUnit.types().get(0).toString();
//					if (type.contains("public class ClassId_2"))
//						b = true;
//					//System.out.println(type);
//				}
//			}
//			if (b = false) {
//				for (CompilationUnit compilationUnit : cus) {
//					System.out.println(compilationUnit);
//				}
//			}
//			
			if (logFiles)
			  logger.logGenerated(cus,checkCompilationErrors);

		}
		
		if (checkCompilationErrors) {
			System.out.println("número de erros de compilação: " + logger.getCompilererrors());
			double per = (logger.getCompilererrors() * 100) / logger.getGeneratedCount(); 
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

}
