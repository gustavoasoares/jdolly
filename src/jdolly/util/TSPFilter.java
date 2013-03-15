package jdolly.util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TSPFilter {
	private static final String REFACTORING = "movemethod";
	private static final String TOOL = "jrrt";

	public static void main(String[] args) {

		String path = "/Users/gustavo/Doutorado/experiments/refactoring-constraints-new/"
				+ REFACTORING + "/last/";
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

		int count = 0;

		List<Precondition> strongPres = new ArrayList<Precondition>();

		for (File test : tests) {
			File in = new File(test, "in");
			File out = new File(test, "out/" + TOOL);

			File tsp = new File(out, "TOO_STRONG");
			if (tsp.exists()) {
				File messageFile = new File(out, "REFACTORING_INAPPLICABLE");
				String message = FileUtil.leArquivo(messageFile
						.getAbsolutePath());		
				message = getMessageTemplate(message);
				String program = Util.getProgram(in);
				program = test.getAbsolutePath() + "\n\n" + program;

				Precondition pre = Precondition.getNamed(message);
				
				if (pre != null) {
					pre.getPrograms().add(program);
					pre.setQnt(pre.getQnt() + 1);					
				} else
					Precondition.loadPrecondition(message, program);
			}
		}	
		int i = 1;
		Enumeration pres = Precondition.getAll().elements();		
		while (pres.hasMoreElements()) {
			Precondition p = (Precondition) pres.nextElement();
			System.out.println(p.getMessage());
			System.out.println(p.getQnt());
			
			//SALVA PROGRAMAS
			String file = path + TOOL + "_PRE" + i;
			String content = p.getMessage() + "\n";
			List<String> programs = p.getPrograms();
			int j = 1;
			for (String program : programs) {
				content = content + "---------------\n";
				content = content + "Program " + j + "\n";
				content = content + program;
				
				j++;
			}
			FileUtil.gravaArquivo(file, content);
			
			System.out.println("----------------");
			i++;
		}
		
		
		
	}

	private static String getMessageTemplate(String message) {
		
		String result = message.replaceAll("'\\S*", "'[]'");
		result = result.replaceAll("'\\n.*", "'");
		return result;
	}

}
