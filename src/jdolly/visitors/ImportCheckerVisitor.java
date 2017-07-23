package jdolly.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ImportCheckerVisitor extends ASTVisitor {
	
	private String className;

	@Override
	public boolean visit(TypeDeclaration node) {
	    setClassName(node.getName().getIdentifier());
		return super.visit(node);
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

}
