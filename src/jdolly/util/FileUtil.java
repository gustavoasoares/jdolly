package jdolly.util;



import java.util.*;
import java.io.*;

public class FileUtil {

	public static void getClasses(String baseDirectoryOfProject, Vector<String> javaFilesNamesFromDirectory,
			String baseNameOfPackage) {
		
		try {
			tryToGetClasses(baseDirectoryOfProject, javaFilesNamesFromDirectory, baseNameOfPackage);
		} catch (Exception e) {
			printErrorInFileUtilMethod("getClasses()");
			e.printStackTrace();
		}
	}

	private static void printErrorInFileUtilMethod(String methodName) {
		System.err.println("Erro no metodo FileUtil." + methodName);
	}

	private static void tryToGetClasses(String path, Vector<String> result,
			String base) {
		File[] files = getFilesFrom(path);
		int totalOfFiles = files.length;
		for (int i = 0; i < totalOfFiles; i++) {
			String actualFileName = files[i].getName();
			String actualFileNameAndHisBasePackage = base + actualFileName;
			if (files[i].isDirectory()) {
				// adicionamos os subdiretorios
				String baseTemp = actualFileNameAndHisBasePackage + StrUtil.DOT_SYMBOL;
				getClasses(files[i].getAbsolutePath(), result, baseTemp);
			} else {
				// so consideramos arquivos java
				final boolean isActualFileJavaFile = actualFileNameAndHisBasePackage.endsWith(StrUtil.CLASS_EXTENSION);
				if (isActualFileJavaFile) {
					String actualFileNameWithoutJavaExtension =  removeJavaExtension(actualFileNameAndHisBasePackage);
					result.add(actualFileNameWithoutJavaExtension);
				}
			}
		}
	}

	private static File[] getFilesFrom(String path) {
		File dir = new File(path);
		File[] arquivos = dir.listFiles();
		return arquivos;
	}

	private static String removeJavaExtension(String arquivo) {
		arquivo = arquivo.replaceAll(".class", "");
		return arquivo;
	}

	public static void print(Vector<String> result) {
		String arquivo;
		for (int i = 0; i < result.size(); i++) {
			arquivo = result.get(i);
			System.out.print(removeJavaExtension(arquivo) + ", ");
		}
		System.out.println();
	}

	public static String leArquivo(String name) {
		String result = StrUtil.EMPTY_STRING;
		try {
			result = readFile(name);
		} catch (Exception e) {
			printErrorInFileUtilMethod("leArquivo()");
			e.printStackTrace();
		}
		return result;
	}

	private static String readFile(String name) throws FileNotFoundException, IOException {
		String result = StrUtil.EMPTY_STRING;
		FileReader fr = new FileReader(new File(name));
		BufferedReader buf = new BufferedReader(fr);
		while (buf.ready()) {
			result += "\n" + buf.readLine();
		}
		return result;
	}

	public static String leArquivoQuebraLinha(String name) {
		String result = StrUtil.EMPTY_STRING;
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(name));
			char[] buf = new char[1024];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				fileData.append(buf, 0, numRead);
			}
			reader.close();
			result = fileData.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;

	}

	public static void gravaArquivo(String name, String texto) {
		try {
			FileWriter fw = new FileWriter(new File(name), false);
			fw.write(texto);
			fw.close();
		} catch (Exception e) {
			printErrorInFileUtilMethod("gravaArquivo()");
			e.printStackTrace();
		}
	}

	// testar o que foi feito
	public static void main(String[] args) {
		runFileUtil();
	}

	private static void runFileUtil() {
		Vector<String> result = new Vector<String>();
		String baseDir = "F:\\eclipse\\analyzer\\src";
		getClasses(baseDir, result, "");
		System.out.println("O diretorio tem " + result.size() + " arquivos.");
		print(result);
		gravaArquivo("F:\\rohit.txt", "valor 1 + ");
		System.out.println("O arquivo lido e: " + leArquivo("F:\\rohit.txt"));
	}

	public static void createFolders() throws Exception {
		String tempDir = System.getProperty("java.io.tmpdir") + "/safeRefactor";
		
		File root = new File(tempDir);
		File build = new File(tempDir + "/buildTests");
		File tests = new File(tempDir + "/tests");
		
		root.mkdir();
		build.mkdir();
		tests.mkdir();		
		
		if (!root.isDirectory() || !build.isDirectory() || !tests.isDirectory())
			throw new Exception("Error while creating temporary folders");

	}
	
	public static void copyFolder(File src, File dest)
	    	throws IOException{
	 
	    	if(src.isDirectory()){
	    		if(!dest.exists()){
	    		   dest.mkdir();
	    		   printSuccessfulCopied("Directory", src, dest);
	    		}
	 
	    		String[] allFiles = src.list();
	 
	    		for (String file : allFiles) {
	    		   File srcFileStructure = new File(src, file);
	    		   File destFileStructure = new File(dest, file);
	    		   
	    		   copyFolder(srcFileStructure,destFileStructure);
	    		}
	    	}else{	    		
	    		copyFile(src, dest);
	    	}
	    }

	private static void copyFile(File src, File dest) throws FileNotFoundException, IOException {
		//Use bytes stream to support all file types
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dest); 
 
		copyFileBitsFromInputStreamToOutputStream(in, out);
 
		in.close();
		out.close();
		
		printSuccessfulCopied("File", src, dest);
	}

	private static void printSuccessfulCopied(String entity, File src, File dest) {
		System.out.println(entity + " copied from " + src + " to " + dest);
	}
	
	public static void copyFileBitsFromInputStreamToOutputStream(
			InputStream in, OutputStream out) throws IOException {
		
		byte[] buffer = new byte[1024];
		int length;
		//copy the file content in bytes 
		while ((length = in.read(buffer)) > 0){
		   out.write(buffer, 0, length);
		}
	}
	
	
	
}
