package oracles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class EclipseOracle extends AbstractOracle {

	@Override
	public void getAllErrors(Map<String, Integer> problems, File test, File target)
			throws FileNotFoundException, IOException {
		FileReader in = new FileReader(target);
		BufferedReader br = new BufferedReader(in);
		String fileLine;

		//pega todos os errors
		String targetFileProblem = "";
		while ((fileLine = br.readLine()) != null) {
			targetFileProblem = removeKeywordsFrom(fileLine);
		}
		// isso tá repetido nos 3 oráculos -> extrair um método.
		updateProblems(problems, test, targetFileProblem);
		in.close();
	}

	@Override
	public String removeKeywordsFrom(String fileLine) {
		return fileLine.replaceAll(" [a-zA-Z0-9]+_[0-9][(]?[\\w]*[)]?", " ");
	}
}
