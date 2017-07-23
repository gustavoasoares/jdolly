package oracles;

public final class OracleFactory {

	public static AbstractOracle createOracleFor(String engineName){
		AbstractOracle oracle;
		
		if (engineName.equals("eclipse")) {
			oracle = new EclipseOracle();
		} else if (engineName.equals("jrrt")) {
			oracle = new JrrtOracle();
		} else {
			oracle = new NetbeansOracle();
		}
		
		return oracle;
	}
}
