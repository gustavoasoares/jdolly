package jdolly.visitors;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.testorrery.IGenerator;

public class FieldVisitor extends ASTVisitor {
	
	private static final String METHOD_NAME = "m_0";

	private static AST ast = AST.newAST(AST.JLS3);

	private IGenerator<Expression> fieldInitGen;

	public FieldVisitor(IGenerator fieldInitGen) {
		super();
		this.fieldInitGen = fieldInitGen;
	}

	@Override
	public boolean visit(FieldDeclaration node) {
	
		VariableDeclarationFragment variable = (VariableDeclarationFragment) node.fragments().get(0);
		variable.setInitializer(fieldInitGen.current());

		return super.visit(node);
	}
}
