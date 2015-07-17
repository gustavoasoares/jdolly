package cdolly.model;

public class VarDeclarationStatement implements Statement {

	private Variable variable;
	private Variable variable2;
	private String value;
	

	public VarDeclarationStatement(Variable variable, String value) {
		super();
		this.variable = variable;
		this.value = value;
	}

	public VarDeclarationStatement(Variable variable, Variable variable2) {
		super();
		this.variable = variable;
		this.variable2 = variable2;
	}

	@Override
	public String getSource() {
		return variable.getType().toString() + " " + variable.getName() + " = " + value + ";";
	}
	
	public Variable getVariable() {
		return variable;
	}



	public void setVariable(Variable variable) {
		this.variable = variable;
	}



	public String getValue() {
		return value;
	}



	public Variable getVariable2() {
		return variable2;
	}

	public void setVariable2(Variable variable2) {
		this.variable2 = variable2;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
