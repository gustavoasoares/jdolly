package jdolly;

public class Scope {

	private int maxPackage;
	private int maxClass;
	private int maxMethod;
	private int maxField;
	
	public Scope(final int maxPackage, final int maxClass, final int maxMethod, final int maxField) {
		this.maxPackage = maxPackage;
		this.maxClass = maxClass;
		this.maxMethod = maxMethod;
		this.maxField = maxField;
	}
	public int getMaxPackage() {
		return maxPackage;
	}
	public void setMaxPackage(final int maxPackage) {
		this.maxPackage = maxPackage;
	}
	public int getMaxClass() {
		return maxClass;
	}
	public void setMaxClass(final int maxClass) {
		this.maxClass = maxClass;
	}
	public int getMaxMethod() {
		return maxMethod;
	}
	public void setMaxMethod(final int maxMethod) {
		this.maxMethod = maxMethod;
	}
	public int getMaxField() {
		return maxField;
	}
	public void setMaxField(final int maxField) {
		this.maxField = maxField;
	}
	
	
}
