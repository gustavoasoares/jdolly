package oracles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class NetbeansOracle implements IOracle {

	@Override
	public void evaluateCorrectness(Map<String, Integer> problems, File test, File target) {
		FileReader in;
		try {
			in = new FileReader(target);
			BufferedReader br = new BufferedReader(in);
			String s;
			String x = "";
			

			while ((s = br.readLine()) != null) {
				if (s.contains("^"))
					continue;
				if (!s.contains("javac"))
					continue;
				if (s.contains("warning"))
					continue;
				if (s.contains("Compiling"))
					continue;
				s = s.replaceAll("[.]*:[1-9]+:", "");
				s = s.replaceAll("[.]*.java", "");
				s = s.replaceAll("P[1-2]_0/Class[1-3]_0", "");
				s = s.replaceAll("P[1-2]_0.Class[1-3]_0", "");
				s = s.replaceAll("[a-z]_0\\([a-z]*\\)", "");
				
				x = x + s + "\n";
				break;
			}
			if (problems.containsKey(x)) {
				Integer integer = problems.get(x);
				integer = integer + 1;
				problems.put(x, integer);
			} else {
				problems.put(x, 1);
				System.out.println(test);
			}

			in.close();


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
