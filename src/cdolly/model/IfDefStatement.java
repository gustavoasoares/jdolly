package cdolly.model;

public class IfDefStatement implements Statement {

	private String elementDefined;
	private VarDeclarationStatement variableDeclarationIf;
	private VarDeclarationStatement variableDeclarationElse;

	/*
	 Example:
	 
	 #ifdef A
	 	int x = 1;
	 #elseif
	 	int x = 3;
	 	
	 */
	

	public IfDefStatement(String elementDefined,
			VarDeclarationStatement variableDeclarationIf,
			VarDeclarationStatement variableDeclarationElse) {
		super();
		this.elementDefined = elementDefined;
		this.variableDeclarationIf = variableDeclarationIf;
		this.variableDeclarationElse = variableDeclarationElse;
	}
	

	public IfDefStatement() {
		super();
	}

	@Override
	public String getSource() {
		StringBuffer ifDefStrBuf = new StringBuffer("  #ifdef " + elementDefined);
		ifDefStrBuf.append("\n" + variableDeclarationIf.getSource());
		if (variableDeclarationElse!=null){
			ifDefStrBuf.append("\n  #else" + "\n" + variableDeclarationElse.getSource());
		}
		ifDefStrBuf.append("\n#endif");
		return ifDefStrBuf.toString();
	}

	public String getElementDefined() {
		return elementDefined;
	}

	public void setElementDefined(String elementDefined) {
		this.elementDefined = elementDefined;
	}

	public VarDeclarationStatement getVariableDeclarationIf() {
		return variableDeclarationIf;
	}

	public void setVariableDeclarationIf(VarDeclarationStatement variableDeclarationIf) {
		this.variableDeclarationIf = variableDeclarationIf;
	}

	public VarDeclarationStatement getVariableDeclarationElse() {
		return variableDeclarationElse;
	}

	public void setVariableDeclarationElse(
			VarDeclarationStatement variableDeclarationElse) {
		this.variableDeclarationElse = variableDeclarationElse;
	}
	
}
