package jdolly.util;



import java.util.*;
import java.io.*;

public class FileUtil {

	/**
	 * @param path
	 *            = diretorio base do projeto
	 * @param result
	 *            = armazenara o nome de todos os arquivos Java do diretorio
	 * @param base
	 *            = eh usado na recursao. Indica o nome base do pacote
	 */
	public static void getClasses(String path, Vector<String> result,
			String base) {
		
		try {
			File dir = new File(path);
			File[] arquivos = dir.listFiles();
			int tam = arquivos.length;
			for (int i = 0; i < tam; i++) {
				if (arquivos[i].isDirectory()) {
					// adicionamos o subdiretorios
					String baseTemp = base + arquivos[i].getName() + ".";
					getClasses(arquivos[i].getAbsolutePath(), result, baseTemp);
				} else {
					// so consideramos arquivos java
					if (arquivos[i].getName().endsWith(".class")) {
						String temp = base + arquivos[i].getName();
						temp = trataNome(temp);
						result.add(temp);
					}
				}
			}
		} catch (Exception e) {
			System.err.println("Erro no metodo FileUtil.getClasses()");
			e.printStackTrace();
		}
	}

	private static String trataNome(String arquivo) {
		// remove a extens�o Java
		arquivo = arquivo.replaceAll(".class", "");
		return arquivo;
	}

	public static void print(Vector<String> result) {
		String arquivo;
		for (int i = 0; i < result.size(); i++) {
			arquivo = result.get(i);
			System.out.print(trataNome(arquivo) + ", ");
		}
		System.out.println();
	}

	public static String leArquivo(String name) {
		String result = "";
		try {
			FileReader fr = new FileReader(new File(name));
			BufferedReader buf = new BufferedReader(fr);
			while (buf.ready()) {
				result += "\n" + buf.readLine();
			}
		} catch (Exception e) {
			System.err.println("Erro no metodo FileUtil.leArquivo()");
			e.printStackTrace();
		}
		return result;
	}

	public static String leArquivoQuebraLinha(String name) {
		String result = "";
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
			System.err.println("Erro no metodo FileUtil.gravaArquivo()");
			e.printStackTrace();
		}
	}

	// testar o que foi feito
	public static void main(String[] args) {
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
	 
	    		//if directory not exists, create it
	    		if(!dest.exists()){
	    		   dest.mkdir();
	    		   System.out.println("Directory copied from " 
	                              + src + "  to " + dest);
	    		}
	 
	    		//list all the directory contents
	    		String files[] = src.list();
	 
	    		for (String file : files) {
	    		   //construct the src and dest file structure
	    		   File srcFile = new File(src, file);
	    		   File destFile = new File(dest, file);
	    		   //recursive copy
	    		   copyFolder(srcFile,destFile);
	    		}
	 
	    	}else{
	    		//if file, then copy it
	    		//Use bytes stream to support all file types
	    		InputStream in = new FileInputStream(src);
	    	        OutputStream out = new FileOutputStream(dest); 
	 
	    	        byte[] buffer = new byte[1024];
	 
	    	        int length;
	    	        //copy the file content in bytes 
	    	        while ((length = in.read(buffer)) > 0){
	    	    	   out.write(buffer, 0, length);
	    	        }
	 
	    	        in.close();
	    	        out.close();
	    	        System.out.println("File copied from " + src + " to " + dest);
	    	}
	    }
}
