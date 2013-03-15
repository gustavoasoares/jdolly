package jdolly.examples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class TestLogger { 
	
	private static final String TEST_NAME_BASE = "test";
	private File logDir;
	private String refactoringName;
	private int generatedCount = 0;
	private int compilererrors = 0;
	private Map<String, Integer> problems = new HashMap<String, Integer>();
	

	public TestLogger(String refactoringName, String logDirPath) {
		this.refactoringName = refactoringName;
		this.logDir = createUniqueLogDir(logDirPath, 0);
	}
	
	public TestLogger(String logDirPath) {
		this.refactoringName = "";
		this.logDir = createUniqueLogDir(logDirPath, 0);
	}

	public void logGenerated(List<CompilationUnit> cus, boolean checkCompilationErrors) {
		setGeneratedCount(getGeneratedCount() + 1);
		String cuSources = "";
		for (CompilationUnit cu : cus) {
			
			String cuName = getPrimaryTypeName(cu);			
			String cuSource = cu.toString();
			String packageName = cu.getPackage().getName().getFullyQualifiedName();
//			String packageName = "";
			outputCompilationUnitFile("in/" + packageName, cuSource, cuName);
			cuSources = cuSources + cuSource;
		}
		if (checkCompilationErrors) {
			String outputMsg = jdolly.util.Compile.compile(getInOutDir("in").toString());
			if (outputMsg.contains("ERROR in")) {
				System.out.println(outputMsg);
				System.out.println(outputMsg);
				for (CompilationUnit cu : cus) {
					System.out.println(cu);
				}
	        	setCompilererrors(getCompilererrors() + 1);        	
	        }	
		}		
	}
	
	protected String getPrimaryTypeName(CompilationUnit cu) {
		String first = null;
		for (Object type : cu.types()) {
			if (type instanceof TypeDeclaration) {
				String typeName = ((TypeDeclaration) type).getName().toString();
				if (first == null) {
					first = typeName;
				}
				for (Object modifier : ((TypeDeclaration) type).modifiers()) {
					if (((Modifier) modifier).getKeyword() == Modifier.ModifierKeyword.PUBLIC_KEYWORD) {
						return typeName;
					}
				}
			}
		}
		return first;
	}
	
	protected void outputCompilationUnitFile(String inOutDirName, String source, String cuName) {
		
		File inOutDir = getInOutDir(inOutDirName);		
		if (!cuName.endsWith(".java")) {
			cuName = cuName + ".java";
		}
		File cuFile = new File(inOutDir, cuName); // ../<compilation_unit_name>.java
		try {
			if (!cuFile.createNewFile()) {
				throw new RuntimeException(String.format("Compilation unit file %s could not be created", cuFile.getAbsolutePath()));			
			}
			FileWriter fw = new FileWriter(cuFile);
			fw.write(source);
			fw.flush();
			fw.close();
		} 
		catch (Exception e) {
			throw new RuntimeException(e); //HACK
		} 			
	}
	
	protected File getInOutDir(String inOutDirName) {

		File inOutDir = new File(getTestDirectory(), inOutDirName); //  <temp_dir>/<refactoring>/<test_dir>/(in|out)
		if (!inOutDir.exists()) {
			if (!inOutDir.mkdirs()) {
				throw new RuntimeException(String.format("In/Out directory %s could not be created", inOutDir.getAbsolutePath()));
			}
		}
		return inOutDir;
	}
	
	public File getTestDirectory() {
		File curTestDir = new File(logDir, TEST_NAME_BASE + getGeneratedCount());
		if (!curTestDir.exists()) {
			if (!curTestDir.mkdirs()) {
				throw new RuntimeException(String.format("Test directory %s could not be created", curTestDir.getAbsolutePath()));
			}
		}			
		return curTestDir;
	}
	
	private File createUniqueLogDir(String logDirPathBase, int suffix) {
		String logDirPath = logDirPathBase + suffix;
		File uniqueLogDir = new File(logDirPath);
		if (uniqueLogDir.exists()) {
			return createUniqueLogDir(logDirPathBase, suffix + 1);
		}
		if (!uniqueLogDir.mkdirs()) {
			throw new RuntimeException(String.format("Log directory %s could not be created", uniqueLogDir.getAbsolutePath()));
		}
		return uniqueLogDir;
	}

	public void setCompilererrors(int compilererrors) {
		this.compilererrors = compilererrors;
	}

	public int getCompilererrors() {
		return compilererrors;
	}

	public void setGeneratedCount(int generatedCount) {
		this.generatedCount = generatedCount;
	}

	public int getGeneratedCount() {
		return generatedCount;
	}

	
	private void categorizeCompError(String error) {
		
		try {
//			FileReader in = new FileReader(target);
			StringReader in = new StringReader(error);
			BufferedReader br = new BufferedReader(in);
			String s;
			String x = "";
			boolean inclueAproxima = true;
			boolean achouErro = false;

			while ((s = br.readLine()) != null) {
				if (s.contains("^"))
					continue;
				// so conta o 1o. erro
				if (s.contains("----------") && achouErro)
					break;
				if (s.contains("ERROR")) {
					inclueAproxima = true;
					achouErro = true;
				}

				if (inclueAproxima) {
					if (s.contains("WARNING"))
						inclueAproxima = false;
					else {
						if (!s.contains("ERROR")
								&& !s.contains("problem")) {
							//para remover nome de classes e metodos
//							s = s
//									.replaceAll(
//											"[(]?[(]?[a-zA-Z0-9]+_[0-9][(]?[(]?[\\w]*[)]?[\\w]*[)]?",
//											" ");
							//para remover o codigo que ocorre o problmea, deixar so a mensagem
							if (s.contains(";"))
								s = "";
							else
								s = s
								.replaceAll(
										"[(]?[(]?[a-zA-Z0-9]+_[0-9][(]?[(]?[\\w]*[)]?[\\w]*[)]?",
										" ");
								
						} else
							s = "ERROR";
						x = x + "\n" + s;

					}

				}
			}
			if (problems.containsKey(x)) {
				Integer integer = problems.get(x);
				integer = integer + 1;
				problems.put(x, integer);
			} else {
				problems.put(x, 1);
//				System.out.println(test);
			}

			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
