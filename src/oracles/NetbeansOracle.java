package oracles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class NetbeansOracle extends AbstractOracle {

	@Override
	public void getAllErrors(Map<String, Integer> problems, File test, File target)
			throws FileNotFoundException, IOException {
		FileReader in = new FileReader(target);
		String targetFileProb = getFileProblemAsString(in);
		
		updateProblems(problems, test, targetFileProb);
		in.close();
	}

	private String getFileProblemAsString(FileReader in) throws IOException {
		BufferedReader br = new BufferedReader(in);
		String fileLine;
		String x = "";
		
		while ((fileLine = br.readLine()) != null) {
			if (shouldSkip(fileLine)) {
				continue;
			}
			fileLine = removeKeywordsFrom(fileLine);

			x = x + fileLine + "\n";
			break;
		}
		return x;
	}

	private boolean shouldSkip(String fileLine) {
		return fileLine.contains("^") || !fileLine.contains("javac") || 
				fileLine.contains("warning") || fileLine.contains("Compiling");
	}
	
	@Override
	public String removeKeywordsFrom(String fileLine) {
		String s = fileLine;
		s = s.replaceAll("[.]*:[1-9]+:", "");
		s = s.replaceAll("[.]*.java", "");
		s = s.replaceAll("P[1-2]_0/Class[1-3]_0", "");
		s = s.replaceAll("P[1-2]_0.Class[1-3]_0", "");
		s = s.replaceAll("[a-z]_0\\([a-z]*\\)", "");
		
		return s;
	}
	
}
