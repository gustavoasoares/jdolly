package cdolly.model;

public enum Relation {
	RETURN_TYPE("returnType"), PARAM("param"), TYPE(
			"type"), STATEMENT("stmt"), RETURN("r"), VARIABLE("v"), VARIABLE2("v2"),
			DEFINE("def"), CMD("cmd"), ELSE("else_"), DEFS("defs");
	
	private String label;

	Relation(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return this.label;
	}

	public String label() {
		return toString();
	}
}
