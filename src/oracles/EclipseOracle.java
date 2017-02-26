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
			FileReader in = new FileReader(target);
			BufferedReader br = new BufferedReader(in);
			String s;
			
			
			//pega só o 1o. erro
//			s = br.readLine();
//			s = s.replaceAll(
//					" [a-zA-Z0-9]+_[0-9][(]?[\\w]*[)]?", " ");
			
			//pega todos os errors
			String x = "";
			while ((s = br.readLine()) != null) {
				s = s.replaceAll(
						" [a-zA-Z0-9]+_[0-9][(]?[\\w]*[)]?", " ");
				x = s;
			}
			if (problems.containsKey(x)) {
				Integer integer = problems.get(x);
				integer = integer + 1;
				problems.put(x, integer);
			} else {
				System.out.println(test);
				
				problems.put(x, 1);
			}

			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
