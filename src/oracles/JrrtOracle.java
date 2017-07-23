package oracles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class JrrtOracle extends AbstractOracle {

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
		String currFileLine;
		String x = "";
		boolean inclueAproxima = true;
		boolean achouErro = false;

		while ((currFileLine = br.readLine()) != null) {
			if (currFileLine.contains("^")) {
				continue;
			}
			// so conta o 1o. erro
			if (currFileLine.contains("----------") && achouErro) {
				break;
			}
			if (currFileLine.contains("ERROR")) {
				inclueAproxima = true;
				achouErro = true;
			} 
			if (inclueAproxima) {
				if (currFileLine.contains("WARNING")) {
					inclueAproxima = false;
				} else {
					if (!currFileLine.contains("ERROR") && !currFileLine.contains("problem")) {
						//para remover o codigo que ocorre o problema, deixar so a mensagem
						if (currFileLine.contains(";")) {
							currFileLine = "";
						} else {
							currFileLine = removeKeywordsFrom(currFileLine);
						}	
					} else {
						currFileLine = "ERROR";
					}
					x = x + "\n" + currFileLine;
				}
			}
		}
		return x;
	}

	@Override
	public String removeKeywordsFrom(String s) {
		String fileLineClean = s.replaceAll("[(]?[(]?[a-zA-Z0-9]+_[0-9][(]?[(]?[\\w]*[)]?[\\w]*[)]?", " ");
		return fileLineClean;
	}
}
