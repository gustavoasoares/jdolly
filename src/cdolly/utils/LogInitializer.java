package cdolly.utils;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

/**
 * Initializes the Logger.
 * 
 * @author Jeanderson Candido<br>
 *         <a target="_blank"
 *         href="http://jeandersonbc.github.io">http://jeandersonbc
 *         .github.io</a>
 * 
 */
public class LogInitializer {

	private static boolean started = false;

	/**
	 * Sets the logger.
	 * 
	 * @throws IOException
	 */
	public static void setup() throws IOException {
		if (started)
			return;

		StringBuilder baseDirPath = new StringBuilder(getLogFile()
				.getAbsolutePath());
		RollingFileAppender rfappender = new RollingFileAppender(
				new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} %c{1} [%p] %m%n"),
				baseDirPath.toString());
		rfappender.setThreshold(Level.INFO);

		BasicConfigurator.configure(rfappender);

		// desligar quando estiver fazendo experimentosW
//		BasicConfigurator.configure(new ConsoleAppender(new PatternLayout(
//				"%d{yyyy-MM-dd HH:mm:ss} %c{1} [%p] %m%n")));

		started = true;
	}

	/**
	 * Gets the log file from "./logs/cdolly.log". If file does not exist, a new
	 * one is created.
	 * 
	 * @return The log file.
	 * @throws IOException
	 */
	private static File getLogFile() throws IOException {
		File logFile = new File("./logs/cdolly.log");
		if (!logFile.exists()) {
			createLogFile(logFile);
		}
		return logFile;
	}

	/**
	 * Creates a new log file into "./logs" directory
	 * 
	 * @param logFile
	 *            The new log file to be created
	 * @throws IOException
	 */
	private static void createLogFile(File logFile) throws IOException {
		File logDir = new File("./logs");
		if (!logDir.exists())
			logDir.mkdir();

		logFile.createNewFile();
	}
}
