package oracles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class JrrtOracle implements IOracle {

	@Override
	public void evaluateCorrectness(Map<String, Integer> problems, File test, File target) {
		try {
			FileReader in = new FileReader(target);
			BufferedReader br = new BufferedReader(in);
			String s;
			String x = "";
			boolean inclueAproxima = true;
			boolean achouErro = false;

			while ((s = br.readLine()) != null) {
				if (s.contains("^"))
					continue;
				// so conta o 1o. erro
				if (s.contains("----------") && achouErro)
					break;
				if (s.contains("ERROR")) {
					inclueAproxima = true;
					achouErro = true;
				}

				if (inclueAproxima) {
					if (s.contains("WARNING"))
						inclueAproxima = false;
					else {
						if (!s.contains("ERROR")
								&& !s.contains("problem")) {
							//para remover nome de classes e metodos
//							s = s
//									.replaceAll(
//											"[(]?[(]?[a-zA-Z0-9]+_[0-9][(]?[(]?[\\w]*[)]?[\\w]*[)]?",
//											" ");
							//para remover o codigo que ocorre o problmea, deixar so a mensagem
							if (s.contains(";"))
								s = "";
							else
								s = s
								.replaceAll(
										"[(]?[(]?[a-zA-Z0-9]+_[0-9][(]?[(]?[\\w]*[)]?[\\w]*[)]?",
										" ");
								
						} else
							s = "ERROR";
						x = x + "\n" + s;

					}

				}
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
