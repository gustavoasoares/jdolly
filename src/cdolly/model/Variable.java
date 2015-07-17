package cdolly.model;

public class Variable {
	private VariableType type;
	private String name;
//	private boolean isPointer = false;
	
	
//	public Variable(VariableType type, String name, boolean isPointer) {
//		super();
//		this.type = type;
//		this.name = name;
////		this.isPointer = isPointer;
//	}
	
	public Variable(VariableType type, String name) {
		super();
		this.type = type;
		this.name = name;
	}
	
	public VariableType getType() {
		return type;
	}
	public void setType(VariableType type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

//	public boolean isPointer() {
//		return isPointer;
//	}
//
//	public void setPointer(boolean isPointer) {
//		this.isPointer = isPointer;
//	}
	
}
