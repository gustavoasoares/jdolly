package jdolly;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class UtilGenerator {

	private static final String METHOD_NAME = "m_0";
	private static AST ast = AST.newAST(AST.JLS3);

	public static List<MethodDeclaration> getMethodsList(final CompilationUnit compilationUnits) {
		final List<MethodDeclaration> result = new ArrayList<MethodDeclaration>();
		final List<TypeDeclaration> types = compilationUnits.types();
		for (final TypeDeclaration type : types) {
			final MethodDeclaration[] methods = type.getMethods();
			for (final MethodDeclaration methodDeclaration : methods) {
				result.add(methodDeclaration);
			}
		}
		return result;
	}

	public static MethodDeclaration getTargetMethod(final CompilationUnit compilationUnits,
			final String className) {
		MethodDeclaration result = null;

		final List<TypeDeclaration> types = compilationUnits.types();
		for (final TypeDeclaration type : types) {
			final MethodDeclaration[] methods = type.getMethods();
			for (final MethodDeclaration methodDeclaration : methods) {
				final TypeDeclaration typeDeclaration = (TypeDeclaration) methodDeclaration
						.getParent();
				if (methodDeclaration.getName().getIdentifier().equals(METHOD_NAME)
						&& typeDeclaration.getName().getIdentifier().equals(
								className)){
					result = methodDeclaration;
				}
			}
		}
		return result;
	}
	
	public static MethodDeclaration getTargetMethod(final CompilationUnit compilationUnits) {
		MethodDeclaration result = null;

		final List<TypeDeclaration> types = compilationUnits.types();
		for (final TypeDeclaration type : types) {
			final MethodDeclaration[] methods = type.getMethods();
			for (final MethodDeclaration methodDeclaration : methods) {
				if (methodDeclaration.getName().getIdentifier().equals(METHOD_NAME)){
					result = methodDeclaration;
				}	
			}
		}
		return result;
	}

	public static List<FieldDeclaration> getFieldsList(final CompilationUnit compilationUnits) {
		final List<FieldDeclaration> result = new ArrayList<FieldDeclaration>();
		final List<TypeDeclaration> types = compilationUnits.types();
		for (final TypeDeclaration type : types) {
			final FieldDeclaration[] fields = type.getFields();
			for (final FieldDeclaration fieldDeclaration : fields) {
				result.add(fieldDeclaration);
			}
		}
		return result;
	}
}
