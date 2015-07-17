package cdolly.model;

public class ReturnStatement implements Statement {

	private String value;
	
	
	public ReturnStatement(String value){
		this.value = value;
	}
	
	@Override
	public String getSource() {
		return "return " + value + ";";
	}

}
