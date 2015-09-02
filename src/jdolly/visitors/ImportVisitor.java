package jdolly.visitors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ImportVisitor extends ASTVisitor {

	private String superClassName;
	private String interfaceName;
	private List<String> fieldTypes = new ArrayList<String>();
	private List<String> instanceTypes = new ArrayList<String>();

	@Override
	public boolean visit(TypeDeclaration node) {
		List superInterfaceTypes = node.superInterfaceTypes();
		if (superInterfaceTypes != null && superInterfaceTypes.size() > 0) {
			interfaceName = superInterfaceTypes.get(0).toString();
		}
		Type superclassType = node.getSuperclassType();
		if (superclassType != null)
			superClassName = superclassType.toString();
		return super.visit(node);
	}
	
	@Override
	public boolean visit(FieldDeclaration node) {
		Type fieldType = node.getType();
		if (fieldType != null)
			getFieldTypes().add(fieldType.toString());
		return super.visit(node);
	}
	@Override
	public boolean visit(ClassInstanceCreation node) {
		Type instanceType = node.getType();
		if (instanceType != null)
			getInstanceTypes().add(instanceType.toString());
			
		return super.visit(node);
	}

	public void setSuperClassName(String superClassName) {
		this.superClassName = superClassName;
	}

	public String getSuperClassName() {
		return superClassName;
	}

	public void setFieldTypes(List<String> fieldTypes) {
		this.fieldTypes = fieldTypes;
	}

	public List<String> getFieldTypes() {
		return fieldTypes;
	}

	public void setInstanceTypes(List<String> instanceTypes) {
		this.instanceTypes = instanceTypes;
	}

	public List<String> getInstanceTypes() {
		return instanceTypes;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}



}
