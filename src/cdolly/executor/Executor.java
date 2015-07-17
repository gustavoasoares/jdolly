package cdolly.executor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import cdolly.experiments.Parameters;
import cdolly.generator.ProgramGenerator;
import cdolly.generator.UnexistingModelException;
import cdolly.model.CFile;
import cdolly.utils.LogInitializer;

public class Executor {

	private static Logger logger = Logger.getLogger(Executor.class);
	private String modelPath;
	private boolean saveInFileSystem = true;
	private long programCount;
	private long skip;

	private Executor(String modelPath, long programCount, long skip) {
		this.modelPath = modelPath;
		this.programCount = programCount;
		this.skip = skip;
	}

	private Executor() {
	}

	public static Executor createExecutor(String alloyModelPath, long programCount, long skip) {
		return new Executor(alloyModelPath, programCount, skip);
	}

	public void run() {
		try {
			LogInitializer.setup();

		} catch (IOException e1) {
			System.err.println(e1.getMessage());
			System.err.println("Aborting...");
			System.exit(1);
		}
		String alloyModelPath = this.modelPath;
		try {
			long initialTime = (new Date()).getTime();
			ProgramGenerator generator = new ProgramGenerator(alloyModelPath);
			
			long programCounter = 1;
			
			File fOutputFolder = null;
			if (saveInFileSystem){
				fOutputFolder = new File(Parameters.ABSOLUTE_SRC_FOLDER);
				if (!fOutputFolder.exists()){
					fOutputFolder.mkdir();
				}
			}

			String program = null;
			int fileCounter = 1;
			int notCompiled = 0;
			long totalCompilationTime = 0;
			StringBuffer compilationErrorPrograms = new StringBuffer();
			
			
			for (List<CFile> programs : generator) {
				programCounter++;
//				System.out.println(programCounter);
//				if (true) continue;
//				if (programCounter > 70000) {
//					System.out.println();
//				}		
				if ((programCounter % skip) != 0) {
					continue;
				}
				logger.info("Program #" + (programCounter) + "\n");
				for (CFile cFile: programs) {
					program = cFile.getCSource();
				}
				if (saveInFileSystem){
					long initialCompilationTime = (new Date()).getTime();
					File programOutputFolder = new File(fOutputFolder.getAbsolutePath() + "/" + (programCounter-1));
					if (!programOutputFolder.exists()){
						programOutputFolder.mkdir();
					}
					try {
//						FileWriter fw = new FileWriter(pFolder.getAbsolutePath() + "/f" + fCount++ + ".c");
						FileWriter fw = new FileWriter(programOutputFolder.getAbsolutePath() + "/f" + fileCounter++ + ".c");
						fw.write(program);
						fw.close();
						//compiling
						boolean compiled = cdolly.utils.Compile.run(programOutputFolder.getAbsolutePath());
						if (!compiled){
							notCompiled++;
							compilationErrorPrograms.append("," + (programCounter-1));
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					long finalCompilationTime = (new Date()).getTime();
					totalCompilationTime += finalCompilationTime - initialCompilationTime;
				}
//				if(programCounter==programCount){
//					break;
//				}
			}
			
			long finalTime = (new Date()).getTime();
			long totalTime  = finalTime - initialTime;
			logger.info("Total execution time (ms): " + totalTime);
			logger.info("Total execution time without compilation (ms): " + (totalTime - totalCompilationTime));
			logger.info("Total #programs: " + (programCounter - 1));
			logger.info("Total #programs not compiled: " + notCompiled);
			logger.info("Programs with compilation error: " + compilationErrorPrograms.toString());
			
			try {
				PrintWriter notCompiledProgramsWriter = new PrintWriter(Parameters.ABSOLUTE_SRC_FOLDER + "/cdolly-programsNotCompiled.properties");
				notCompiledProgramsWriter.print(compilationErrorPrograms.toString());
				notCompiledProgramsWriter.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		} catch (UnexistingModelException e) {
			e.printStackTrace();
		}
	}

}
