package jdolly;

import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class UtilGenerator {

	private static final String METHOD_NAME = "m_0";
	private static AST ast = AST.newAST(AST.JLS3);

	private UtilGenerator(){}

	public static List<MethodDeclaration> getMethodsList(
			final CompilationUnit compilationUnits) {

		final List<MethodDeclaration> result = new ArrayList<MethodDeclaration>();
		final List<TypeDeclaration> types = compilationUnits.types();
		for (final TypeDeclaration type : types) {
			final MethodDeclaration[] methods = type.getMethods();
			result.addAll(Arrays.asList(methods));
		}
		return result;
	}

	public static MethodDeclaration getTargetMethod(
			final CompilationUnit compilationUnits, final String className) {
		MethodDeclaration result = null;

		final List<TypeDeclaration> types = compilationUnits.types();
		for (final TypeDeclaration type : types) {
			final MethodDeclaration[] methods = type.getMethods();
			for (final MethodDeclaration methodDeclaration : methods) {
				final TypeDeclaration typeDeclaration = (TypeDeclaration) methodDeclaration
						.getParent();
				if (isMethodName(methodDeclaration)
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
				if (isMethodName(methodDeclaration)) {
					result = methodDeclaration;
				}	
			}
		}
		return result;
	}

	private static boolean isMethodName(final MethodDeclaration methodDeclaration) {
		return methodDeclaration.getName().getIdentifier().equals(METHOD_NAME);
	}

	public static List<FieldDeclaration> getFieldsList(final CompilationUnit compilationUnits) {
		final List<FieldDeclaration> result = new ArrayList<FieldDeclaration>();
		final List<TypeDeclaration> types = compilationUnits.types();
		for (final TypeDeclaration type : types) {
			final FieldDeclaration[] fields = type.getFields();
			result.addAll(Arrays.asList(fields));
		}
		return result;
	}
}
