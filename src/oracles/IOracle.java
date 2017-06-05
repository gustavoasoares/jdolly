package oracles;

import java.io.File;
import java.util.Map;

public interface IOracle {
	
	void evaluateCorrectness(Map<String, Integer> problems, File test,	File target);
}
