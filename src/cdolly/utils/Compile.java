package cdolly.utils;
import java.io.File;

import org.apache.log4j.Logger;

import cdolly.experiments.Parameters;


public class Compile {

	private static Logger logger = Logger.getLogger(Compile.class);
	


	public static boolean run(String programPath) {

		Util.executaComando("cp " +Parameters.CDOLLY_RESOURCES+"/script_seq.sh "
				
							+ programPath, null);
		//copying main.c to be possible compile a program
		Util.executaComando("cp "+Parameters.CDOLLY_RESOURCES+"/main.c "
				+ programPath, null);

		
		File scriptPath = new File(programPath + "/script_seq.sh");
		boolean executouComandoCorretamente = Util.executaComando(scriptPath.getAbsolutePath()
				+ " a.out " + programPath + " " + "c99" + " " + "clang" + " " + " ", null);

		//removing files
		Util.executaComando("rm " + programPath + "/script_seq.sh ", null);
		Util.executaComando("rm " + programPath + "/main.c ", null);
		if (executouComandoCorretamente){
			Util.executaComando("rm " + programPath + "/a.out ", null);
			return true;
		}

        return false;        
	}

}
