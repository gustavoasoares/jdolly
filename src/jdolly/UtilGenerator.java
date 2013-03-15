package jdolly;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;



public class UtilGenerator {

	private static final String METHOD_NAME = "m_0";
	private static AST ast = AST.newAST(AST.JLS3);

	

	public static List<MethodDeclaration> getMethodsList(CompilationUnit cu) {
		List<MethodDeclaration> result = new ArrayList<MethodDeclaration>();
		List<TypeDeclaration> types = cu.types();
		for (TypeDeclaration type : types) {
			MethodDeclaration[] methods = type.getMethods();
			for (MethodDeclaration methodDeclaration : methods) {
				CharSequence getterMethod = "get";
				
			//	if (!methodDeclaration.getName().getIdentifier().equals(
			//			"Test_0")
			//			&& !methodDeclaration.getName().getIdentifier().equals(
		//						METHOD_NAME) && !methodDeclaration.getName().getIdentifier().contains(getterMethod))
					result.add(methodDeclaration);
			}

		}
		return result;
	}

	public static MethodDeclaration getTargetMethod(CompilationUnit cu,
			String className) {
		MethodDeclaration result = null;

		// for (MethodDeclaration methodDeclaration : methods) {
		// if (methodDeclaration.getParent() instanceof TypeDeclaration) {
		// TypeDeclaration typeDeclaration = (TypeDeclaration)
		// methodDeclaration.getParent();
		// if (methodDeclaration.getName().getIdentifier().equals("M_0") &&
		// typeDeclaration.getName().getIdentifier().equals(className))
		// result = methodDeclaration;
		// }
		// }
		List<TypeDeclaration> types = cu.types();
		for (TypeDeclaration type : types) {
			MethodDeclaration[] methods = type.getMethods();
			for (MethodDeclaration methodDeclaration : methods) {
				TypeDeclaration typeDeclaration = (TypeDeclaration) methodDeclaration
						.getParent();
				if (methodDeclaration.getName().getIdentifier().equals(METHOD_NAME)
						&& typeDeclaration.getName().getIdentifier().equals(
								className))
					result = methodDeclaration;
			}

		}

		return result;
	}
	
	public static MethodDeclaration getTargetMethod(CompilationUnit cu) {
		MethodDeclaration result = null;

		// for (MethodDeclaration methodDeclaration : methods) {
		// if (methodDeclaration.getParent() instanceof TypeDeclaration) {
		// TypeDeclaration typeDeclaration = (TypeDeclaration)
		// methodDeclaration.getParent();
		// if (methodDeclaration.getName().getIdentifier().equals("M_0") &&
		// typeDeclaration.getName().getIdentifier().equals(className))
		// result = methodDeclaration;
		// }
		// }
		List<TypeDeclaration> types = cu.types();
		for (TypeDeclaration type : types) {
			MethodDeclaration[] methods = type.getMethods();
			for (MethodDeclaration methodDeclaration : methods) {
				TypeDeclaration typeDeclaration = (TypeDeclaration) methodDeclaration
						.getParent();
				if (methodDeclaration.getName().getIdentifier().equals(METHOD_NAME))
					result = methodDeclaration;
			}

		}

		return result;
	}

	public static List<FieldDeclaration> getFieldsList(CompilationUnit cu) {
		List<FieldDeclaration> result = new ArrayList<FieldDeclaration>();
		List<TypeDeclaration> types = cu.types();
		for (TypeDeclaration type : types) {
			FieldDeclaration[] fields = type.getFields();
			for (FieldDeclaration fieldDeclaration : fields) {
				//Type fieldType = fieldDeclaration.getType();
				//if (fieldType.toString().equals("long") || fieldType.toString().equals("int"))
				result.add(fieldDeclaration);
			}
		}
		return result;
	}
}
