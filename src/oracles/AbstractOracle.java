package oracles;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public abstract class AbstractOracle {
	
	public void updateProblems(Map<String, Integer> problems, File test, String targetFileProb) {
		if (problems.containsKey(targetFileProb)) {
			Integer integer = problems.get(targetFileProb);
			integer = integer + 1;
			problems.put(targetFileProb, integer);
		} else {
			problems.put(targetFileProb, 1);
			System.out.println(test);
		}
	}
	
	public void evaluateCorrectness(Map<String, Integer> problems, File test, File target) {
		try {
			getAllErrors(problems, test, target);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public abstract void getAllErrors(Map<String, Integer> problems, File test,	File target) throws FileNotFoundException, IOException;
	
	public abstract String removeKeywordsFrom(String fileLine);
}
