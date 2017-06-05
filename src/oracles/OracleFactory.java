package oracles;

public final class OracleFactory {

	public static IOracle createOracleFor(String engineName){
		IOracle oracle;
		if(engineName.equals("eclipse")){
			oracle = new EclipseOracle();
		}
		else if(engineName.equals("jrrt")){
			oracle = new JrrtOracle();
		}
		else{
			oracle = new NetbeansOracle();
		}
		return oracle;
	}
}
