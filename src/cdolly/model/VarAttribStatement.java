package cdolly.model;

public class VarAttribStatement extends VarDeclarationStatement{


	public VarAttribStatement(Variable variable, String value) {
		super(variable, value);
	}
	
	public VarAttribStatement(Variable variable, Variable variable2) {
		super(variable, variable2);
	}
	
	@Override
	public String getSource() {
		if (super.getValue() == null) {
			return super.getVariable().getName() + " = " + super.getVariable2().getName() + ";";
		} else {
			return super.getVariable().getName() + " = " + super.getValue() + ";";
		}
	}

}
