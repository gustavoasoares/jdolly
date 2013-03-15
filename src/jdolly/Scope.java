package jdolly;

public class Scope {

	private int maxPackage;
	private int maxClass;
	private int maxMethod;
	private int maxField;
	
	public Scope(int maxPackage, int maxClass, int maxMethod, int maxField) {
		this.maxPackage = maxPackage;
		this.maxClass = maxClass;
		this.maxMethod = maxMethod;
		this.maxField = maxField;
	}
	public int getMaxPackage() {
		return maxPackage;
	}
	public void setMaxPackage(int maxPackage) {
		this.maxPackage = maxPackage;
	}
	public int getMaxClass() {
		return maxClass;
	}
	public void setMaxClass(int maxClass) {
		this.maxClass = maxClass;
	}
	public int getMaxMethod() {
		return maxMethod;
	}
	public void setMaxMethod(int maxMethod) {
		this.maxMethod = maxMethod;
	}
	public int getMaxField() {
		return maxField;
	}
	public void setMaxField(int maxField) {
		this.maxField = maxField;
	}
	
	
}
