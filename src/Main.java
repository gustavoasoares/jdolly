


public class Main {

	public static void main(String[] args) {
		
		
		parseArguments(args);
		

	}

	private static void parseArguments(String[] args) {
		String arg;
		int i = 0;
		while (i < args.length ) {
			arg = args[i++];
			if (arg.equals("-cdolly")) {
				cdolly.Main.parseArguments(args);
				cdolly.Main.run();
				break;
			}
			if (arg.equals("-jdolly")) {
				jdolly.main.Main.run(args);
				break;
			}
		}
		
	}
	
	
}
