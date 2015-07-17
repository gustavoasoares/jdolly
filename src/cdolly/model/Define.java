package cdolly.model;

public class Define implements Statement{
	
	private String elementDefined; //ex.: #define a;  #define A 1

	public Define(String elementDefined) {
		super();
		this.elementDefined = elementDefined;
	}

	public String getElementDefined() {
		return elementDefined;
	}

	public void setElementDefined(String elementDefined) {
		this.elementDefined = elementDefined;
	}

	@Override
	public String getSource() {
		return "#define " + elementDefined;
	}
	
}
