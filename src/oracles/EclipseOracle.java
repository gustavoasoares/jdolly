package oracles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class EclipseOracle implements IOracle {

	@Override
	public void evaluateCorrectness(Map<String, Integer> problems, File test, File target) {
		try {
			getAllErros(problems, test, target);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getAllErros(Map<String, Integer> problems, File test, File target)
			throws FileNotFoundException, IOException {
		FileReader in = new FileReader(target);
		BufferedReader br = new BufferedReader(in);
		String fileLine;
		//pega só o 1o. erro
//			s = br.readLine();
//			s = s.replaceAll(
//					" [a-zA-Z0-9]+_[0-9][(]?[\\w]*[)]?", " ");
		
		//pega todos os errors
		String targetFileProblem = "";
		while ((fileLine = br.readLine()) != null) {
			targetFileProblem = removeKeywordsFrom(fileLine);
		}
		if (problems.containsKey(targetFileProblem)) {
			Integer currTotalOfProb = problems.get(targetFileProblem);
			Integer updatedTotalOfProb = currTotalOfProb + 1;
			problems.put(targetFileProblem, updatedTotalOfProb);
		} else {
			problems.put(targetFileProblem, /* initial value = */ 1);
			System.out.println(test);
		}
		in.close();
	}

	private String removeKeywordsFrom(String fileLine) {
		return fileLine.replaceAll(" [a-zA-Z0-9]+_[0-9][(]?[\\w]*[)]?", " ");
	}
}
