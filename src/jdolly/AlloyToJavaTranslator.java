package jdolly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import jdolly.visitors.ImportCheckerVisitor;
import jdolly.visitors.ImportVisitor;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;


import edu.mit.csail.sdg.alloy4.SafeList;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig.Field;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;

public class AlloyToJavaTranslator {

	// private static final String ALLOY_MODULE_NAME = "javametamodel";

	public AlloyToJavaTranslator(A4Solution ans) {
		this.ans = ans;
	}
 
	private static AST ast = AST.newAST(AST.JLS3); 

	private A4Solution ans;

	public List<CompilationUnit> getJavaCode() {

		List<CompilationUnit> result = new ArrayList<CompilationUnit>();

		for (String c : getClasses()) {

			PackageDeclaration packageDeclaration = getPackage(c);

			// List<ImportDeclaration> importDeclarations = getImports(c);

			TypeDeclaration typeDeclaration = getClass(c);

			CompilationUnit compilationUnit = ast.newCompilationUnit();

			compilationUnit.setPackage(packageDeclaration);

			// for (ImportDeclaration importDeclaration : importDeclarations) {
			// compilationUnit.imports().add(importDeclaration);
			// }

			compilationUnit.types().add(typeDeclaration);

			result.add(compilationUnit);
		}

		// colocar imports
		for (CompilationUnit cu : result) {
			ImportVisitor cv = new ImportVisitor();
			cu.accept(cv);
			Name packageName = cu.getPackage().getName();
			// System.out.println("Superclass: " + cv.getSuperClassName());

			List<String> importedPackages = new ArrayList<String>();

			for (CompilationUnit cu2 : result) {
				Name packageToCompare = cu2.getPackage().getName();
				ImportDeclaration importDeclaration = null;
				if (packageName.getFullyQualifiedName().equals(
						packageToCompare.getFullyQualifiedName()))
					continue;
				ImportCheckerVisitor iv = new ImportCheckerVisitor();
				cu2.accept(iv);

				// adiciona o import para super class
				if (iv.getClassName().equals(cv.getSuperClassName())) {
					importDeclaration = importPacakge(packageToCompare);

				}
				// adiciona o import para fields
				else {
					for (String fieldType : cv.getFieldTypes()) {
						if (iv.getClassName().equals(fieldType)) {
							importDeclaration = importPacakge(packageToCompare);
							break;
						}
					}
					if (importDeclaration == null) {
						for (String instanceType : cv.getInstanceTypes()) {
							if (iv.getClassName().equals(instanceType)) {
								importDeclaration = importPacakge(packageToCompare);
								break;
							}
						}
					}

				}

				if (importDeclaration != null
						&& !importedPackages.contains(packageToCompare
								.getFullyQualifiedName())) {
					importedPackages.add(packageToCompare
							.getFullyQualifiedName());
					cu.imports().add(importDeclaration);

				}

			}

		}

		return result;
	}

	private ImportDeclaration importPacakge(Name packageToCompare) {
		ImportDeclaration importDeclaration = ast.newImportDeclaration();
		importDeclaration.setName(ast.newSimpleName(packageToCompare
				.getFullyQualifiedName()));
		importDeclaration.setOnDemand(true);
		return importDeclaration;
	}

	private List<String> getClasses() {
		List<String> result = new ArrayList<String>();

		Sig sig = getSig("Class");
		List<String> classInstances = extractInstances(ans.eval(sig).toString());
		result.addAll(classInstances);

		return result;
	}

	private TypeDeclaration getClass(String classId) {
		TypeDeclaration result = ast.newTypeDeclaration();

		Sig classSig = getSig("Class");
		SafeList<Field> classFields = classSig.getFields();
		Field classIdRelations = getField("id", classFields);
		Map<String, List<String>> classIdRel = getRelations(classIdRelations);

		Field classExtendRel = getField("extend", classFields);
		Map<String, List<String>> extendRel = getRelations(classExtendRel);
		
		Field classIsInterfaceRel = getField("isInterface", classFields);
		
		
		Field classIsAbstractRel = getField("isAbstract", classFields);
	
		Field classImplementRel = getField("implement", classFields);
		if (classImplementRel != null) {
			Map<String, List<String>> implementRel = getRelations(classImplementRel);
			
			List<String> interface_ = implementRel.get(classId);
			if (interface_ != null) {
				String interfaceName = classIdRel.get(interface_.get(0)).get(0);
				result.superInterfaceTypes().add(ast.newSimpleType(ast
						.newSimpleName(interfaceName)));
			}
		}
		//isInterface
		if (classIsInterfaceRel != null) {
			Map<String, List<String>> IsInterfacRel = getRelations(classIsInterfaceRel);
			String isInterface = IsInterfacRel.get(classId).get(0);
			boolean isInterface_ = isInterface.contains("True");
			if (isInterface_) {
				result.setInterface(true);
			}
			// methods
			List<MethodDeclaration> methods = getClassMethods(classFields, classId,isInterface_);
			for (MethodDeclaration methodDeclaration : methods) {
				result.bodyDeclarations().add(methodDeclaration);
			}
		} else {
			// methods
			List<MethodDeclaration> methods = getClassMethods(classFields, classId,false);
			for (MethodDeclaration methodDeclaration : methods) {
				result.bodyDeclarations().add(methodDeclaration);
			}
		}

		//isInterface
		if (classIsAbstractRel != null) {
			Map<String, List<String>> IsAbstractRel = getRelations(classIsAbstractRel);
			String isAbstract = IsAbstractRel.get(classId).get(0);
			boolean isAbstract_ = isAbstract.contains("True");
			if (isAbstract_) {
				result.modifiers().add(ast.newModifier(ModifierKeyword.ABSTRACT_KEYWORD));
			}
		}
	
		// id
		String className = classIdRel.get(classId).get(0);
		result.setName(ast.newSimpleName(className));

		// visibilidade publica

		result.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));

		// heran�a
		List<String> superClass = extendRel.get(classId);
		if (superClass != null) {
			String superClassName = classIdRel.get(superClass.get(0)).get(0);
			result.setSuperclassType(ast.newSimpleType(ast
					.newSimpleName(superClassName)));
		}

		// atributos
		List<FieldDeclaration> fields = getClassFields(classFields, classId);
		for (FieldDeclaration fieldDeclaration : fields) {
			result.bodyDeclarations().add(fieldDeclaration);
		}



		return result;
	}

	private List<MethodDeclaration> getClassMethods(
			SafeList<Field> classFields, String classId, boolean isInterface) {
		List<MethodDeclaration> result = new ArrayList<MethodDeclaration>();

		Field methodsRel = getField("methods", classFields);

		Map<String, List<String>> methodsMap = getRelations(methodsRel);

		List<String> methods = methodsMap.get(classId);

		if (methods != null) {
			Sig methodSig = getSig("Method");
			SafeList<Field> mFields = methodSig.getFields();
			Field mIdRelations = getField("id", mFields);
			Map<String, List<String>> idRel = getRelations(mIdRelations);
			Field mIsAbstractRelations = getField("isAbstract", mFields);
			Field mArgRelations = getField("param", mFields);
			Map<String, List<String>> argRel = getRelations(mArgRelations);
			Field mVisRelations = getField("acc", mFields);
			Map<String, List<String>> visRel = getRelations(mVisRelations);
			Field mBodyRelations = getField("b", mFields);

			Map<String, List<String>> bodyRel = getRelations(mBodyRelations);

			for (String method : methods) {
				String id = idRel.get(method).get(0);
				String arg = "";
				if (argRel.containsKey(method))
					arg = argRel.get(method).get(0);

				String vis = "";
				if (visRel.containsKey(method))
					vis = visRel.get(method).get(0);

				String isAbstract = "";
				if (mIsAbstractRelations != null) {
					Map<String, List<String>> isAbstractRel = getRelations(mIsAbstractRelations);
					
					if (isAbstractRel.containsKey(method))
						isAbstract = isAbstractRel.get(method).get(0);
				} else {
					isAbstract = "False";
				}
				
				MethodDeclaration methodDeclaration = ast
						.newMethodDeclaration();
				methodDeclaration.setName(ast.newSimpleName(id.toLowerCase()));
				Modifier m = getAccessModifier(vis);
				if (m != null)
					methodDeclaration.modifiers().add(m);

				if (isAbstract.contains("True") && !isInterface) {
					Modifier mAbstrct = getAccessModifier("abstract");
					methodDeclaration.modifiers().add(mAbstrct);
				}
				
				if (arg.length() > 0) {
					SingleVariableDeclaration parameter = ast
							.newSingleVariableDeclaration();
					parameter.setName(ast.newSimpleName("a"));
					parameter.setType(getType(arg));
					methodDeclaration.parameters().add(parameter);
				}

				methodDeclaration.setReturnType2(ast
						.newPrimitiveType(PrimitiveType.LONG));
				List<String> list = bodyRel.get(method);
				if (list != null) {
					String body = bodyRel.get(method).get(0);
					ReturnStatement returnStatement = ast.newReturnStatement();
					returnStatement.setExpression(getMethodBody(body, classId));
					methodDeclaration.setBody(ast.newBlock());
					methodDeclaration.getBody().statements().add(returnStatement);
				}
				result.add(methodDeclaration);
			}
		}

		return result;
	}

	private Expression getMethodBody(String bodyId, String classId) {

		// String instanceName = bodyId.replaceAll("_[0-9]", "");

		Sig s = null;
		SafeList<Field> sFields = null;

		// pega o nome da classe que contem o metodo para usar no qualified this
		Sig classSig = getSig("Class");
		SafeList<Field> classFields = classSig.getFields();
		Field classIdRelations = getField("id", classFields);
		Map<String, List<String>> classIdRel = getRelations(classIdRelations);

		//debug
//		System.out.println("Body id: " + bodyId);
		
		if (isInstanceOfType(bodyId, "MethodInvocation")) {
			s = getSig("MethodInvocation");
			sFields = s.getFields();
			Field idRelations = getField("id", sFields);
			String methodId = getFieldInstance(idRelations, bodyId);
			Field qualRelations = getField("q", sFields);
			String qualifier = getFieldInstance(qualRelations, bodyId);
			String className = classIdRel.get(classId).get(0);
			return getMethodInvocationExpression(methodId, qualifier, className);

		} else if (isInstanceOfType(bodyId, "ConstructorMethodInvocation")) {
			s = getSig("ConstructorMethodInvocation");
			sFields = s.getFields();
			Field idRelations = getField("idMethod", sFields);
			String methodId = getFieldInstance(idRelations, bodyId);
			Field idClassRelations = getField("idClass", sFields);
			String className = getFieldInstance(idClassRelations, bodyId);
			return getConstructorMethodInvocationExpression(methodId, className);
		} else if (isInstanceOfType(bodyId, "FieldInvocation")) {
			s = getSig("FieldInvocation");
			sFields = s.getFields();
			Field idRelations = getField("id", sFields);
			String fieldId = getFieldInstance(idRelations, bodyId);
			Field qualRelations = getField("q", sFields);
			String qualifier = getFieldInstance(qualRelations, bodyId);
			String className = classIdRel.get(classId).get(0);
			return getFieldInvocationExpression(fieldId, qualifier, className);
		} else if (isInstanceOfType(bodyId, "ConstructorFieldInvocation")) {
			s = getSig("ConstructorFieldInvocation");
			sFields = s.getFields();
			Field idRelations = getField("idField", sFields);
			String fieldId = getFieldInstance(idRelations, bodyId);
			Field idClassRelations = getField("idClass", sFields);
			String className = getFieldInstance(idClassRelations, bodyId);
			return getConstructorFieldInvocationExpression(fieldId, className);
		} else {
			// System.out.println(bodyId);
			// Random generator = new Random();
			// int value = generator.nextInt(100);
			String value = bodyId.toString();
			value = value.replaceAll("(.)*_", "");
			return ast.newNumberLiteral(value);
		}

	}

	private boolean isInstanceOfType(String instance, String type) {
		Sig s = getSig(type);
		
		//debug 
//		System.out.println("type: " + s);
		
		if (s == null)
			return false;

		SafeList<Field> sFields = s.getFields();
		Field idRelations = getField("id", sFields);
		
		//debug
//		System.out.println("sFieds: " + sFields);
//		System.out.println("idRelations: "  + idRelations);
		
		String methodId = getFieldInstance(idRelations, instance);
		
		if (!methodId.equals(""))
			return true;
		return false;
	}

	private Expression getConstructorMethodInvocationExpression(
			String methodId, String classId) {
		Expression result = null;
		ClassInstanceCreation instExpr = ast.newClassInstanceCreation();
		instExpr.setType(ast.newSimpleType(ast.newSimpleName(classId)));

		Expression parameter = createParameter(methodId);

		SimpleName methodName = ast.newSimpleName(methodId.toLowerCase());

		MethodInvocation methInv = ast.newMethodInvocation();
		methInv.setName(methodName);
		if (parameter != null)
			methInv.arguments().add(parameter);

		methInv.setExpression(instExpr);

		result = methInv;

		return result;
	}

	private Expression getConstructorFieldInvocationExpression(String fieldId,
			String classId) {
		Expression result = null;

		ClassInstanceCreation instExpr = ast.newClassInstanceCreation();
		instExpr.setType(ast.newSimpleType(ast.newSimpleName(classId)));
		FieldAccess fieldAcc = ast.newFieldAccess();
		fieldAcc.setExpression(instExpr);
		fieldAcc.setName(ast.newSimpleName(fieldId.toLowerCase()));
		result = fieldAcc;
		return result;
	}

	private String getFieldInstance(Field f, String key) {
		String result = "";

		Map<String, List<String>> idRel = getRelations(f);
		
//		if (idRel.size() > 0) {
//			Collection<List<String>> values = idRel.values();
//			for (List<String> list : values) {
//				if (list.contains(key)) result = list.get(list.indexOf(key));
//			}
//		}
		if (idRel.size() > 0 && idRel.containsKey(key))

			result = idRel.get(key).get(0);

		return result;
	}

	private Expression getMethodInvocationExpression(String methodId,
			String qualifier, String classId) {
		Expression result = null;

		Expression parameter = createParameter(methodId, classId);

		SimpleName methodName = ast.newSimpleName(methodId.toLowerCase());

		if (qualifier.equals("super__0")) {
			SuperMethodInvocation superMethInv = ast.newSuperMethodInvocation();
			superMethInv.setName(methodName);
			if (parameter != null)
				superMethInv.arguments().add(parameter);
			result = superMethInv;
		} else {
			MethodInvocation methInv = ast.newMethodInvocation();
			methInv.setName(methodName);
			if (parameter != null)
				methInv.arguments().add(parameter);
			if (qualifier.equals("this__0")) {
				ThisExpression thisExpression = ast.newThisExpression();
				// radom
				// o this pode ser qualificado ou nao
				// Random generator = new Random();
				// int value = generator.nextInt(2);
				// if (value == 1)
				// thisExpression.setQualifier(ast.newSimpleName(classId));
				methInv.setExpression(thisExpression);
			}
			if (qualifier.equals("qthis__0")) {
				ThisExpression thisExpression = ast.newThisExpression();
				thisExpression.setQualifier(ast.newSimpleName(classId));
				methInv.setExpression(thisExpression);
			}
			result = methInv;
		}

		return result;
	}

	private Expression createParameter(String methodId, String classId) {
		Sig classSig = getSig("Class");
		SafeList<Field> cFields = classSig.getFields();
		Field cIdRelations = getField("id", cFields);
		Field cMethodsRelations = getField("methods", cFields);
		
		Sig methodSig = getSig("Method");
		
		SafeList<Field> mFields = methodSig.getFields();
		Field mIdRelations = getField("id", mFields);
		
		Map<String, List<String>> idRel = getRelations(mIdRelations);
		
		Field mArgRelations = getField("param", mFields);
		Map<String, List<String>> argRel = getRelations(mArgRelations);

		String parameterType = "";
		boolean someMethodwithoutParameter = false;
		for (String s : idRel.keySet()) {
//			System.out.println(s);
			String methodId2 = idRel.get(s).get(0);
//			System.out.println(methodId2);
			
			if (idRel.get(s).size() > 0 && methodId2.equals(methodId)) {
				//HACK
				if (argRel.containsKey(s)) {
//					System.out.println("tem");
					parameterType = argRel.get(s).get(0);
					//break;
				} else {
					someMethodwithoutParameter = true;
				}

			}
		}
		if (someMethodwithoutParameter) parameterType = "";
//		System.out.println("=================");

		Expression parameter = null;
		if (parameterType.equals("Int__0") || parameterType.equals("Long__0")) {
			parameter = ast.newNumberLiteral("2");
		} else if (parameterType.length() > 0)
			parameter = ast.newNullLiteral();
		return parameter;
	}

	private Expression createParameter(String methodId) {
		// pegar parametros dos metodo
		Sig methodSig = getSig("Method");
		SafeList<Field> mFields = methodSig.getFields();
		Field mIdRelations = getField("id", mFields);
		Map<String, List<String>> idRel = getRelations(mIdRelations);
		Field mArgRelations = getField("param", mFields);
		Map<String, List<String>> argRel = getRelations(mArgRelations);

		String parameterType = "";
		for (String s : idRel.keySet()) {
			if (idRel.get(s).size() > 0 && idRel.get(s).get(0).equals(methodId)) {
				if (argRel.containsKey(s)) {
					parameterType = argRel.get(s).get(0);
					break;
				}

			}
		}

		Expression parameter = null;
		if (parameterType.equals("Int__0") || parameterType.equals("Long__0")) {
			parameter = ast.newNumberLiteral("2");
		} else if (parameterType.length() > 0)
			parameter = ast.newNullLiteral();
		return parameter;
	}

	private Expression getFieldInvocationExpression(String fieldId,
			String qualifier, String classId) {

		Expression result = null;

		SimpleName fieldName = ast.newSimpleName(fieldId.toLowerCase());
		result = fieldName;
		
		if (qualifier.equals("super__0")) {
			SuperFieldAccess superFieldAcc = ast.newSuperFieldAccess();
			superFieldAcc.setName(fieldName);
			result = superFieldAcc;
		} else {
			if (qualifier.equals("this__0")) {
				FieldAccess fieldAcc = ast.newFieldAccess();			
				fieldAcc.setName(fieldName);
				ThisExpression thisExpression = ast.newThisExpression();				
				fieldAcc.setExpression(thisExpression);
				result = fieldAcc;
			}
			else if (qualifier.equals("qthis__0")) {
				FieldAccess fieldAcc = ast.newFieldAccess();			
				fieldAcc.setName(fieldName);
				ThisExpression thisExpression = ast.newThisExpression();
				thisExpression.setQualifier(ast.newSimpleName(classId));
				fieldAcc.setExpression(thisExpression);
				result = fieldAcc;
			}
		}
		return result;
		
	}

	private BodyType setBodyType(String bodySig) {
		BodyType result = null;

		if (bodySig.startsWith("MethodInvocation"))
			result = BodyType.METHOD_INVOCATION;
		else if (bodySig.startsWith("FieldInvocation"))
			result = BodyType.FIELD_INVOCATION;
		else if (bodySig.startsWith("ConstructorFieldInvocation"))
			result = BodyType.CONSTRUCTOR_FIELD_INVOCATION;
		else if (bodySig.startsWith("ConstructorMethodInvocation"))
			result = BodyType.CONSTRUCTOR_METHOD_INVOCATION;

		// TODO
		// Isso vai ser aleat�rio
		// else if (bodySig.startsWith("ClassMethodInvocation"))
		// result = BodyType.CLASS_METHOD_INVOCATION;
		// else if (bodySig.startsWith("ClassFieldInvocation"))
		// result = BodyType.CLASS_FIELD_INVOCATION;
		else if (bodySig.startsWith("LiteralValue"))
			result = BodyType.INT_CONSTANT_VALUE;
		return result;
	}

	private List<FieldDeclaration> getClassFields(SafeList<Field> classFields,
			String td) {
		List<FieldDeclaration> result = new ArrayList<FieldDeclaration>();

		Field fieldsRel = getField("fields", classFields);

		Map<String, List<String>> fieldsMap = getRelations(fieldsRel);

		List<String> fields = fieldsMap.get(td);

		if (fields != null) {
			Sig fieldSig = getSig("Field");
			SafeList<Field> fFields = fieldSig.getFields();
			Field fIdRelations = getField("id", fFields);
			Map<String, List<String>> idRel = getRelations(fIdRelations);
			Field fTypeRelations = getField("type", fFields);
			Map<String, List<String>> typeRel = getRelations(fTypeRelations);
			Field fVisRelations = getField("acc", fFields);
			Map<String, List<String>> visRel = getRelations(fVisRelations);

			for (String field : fields) {
				String id = idRel.get(field).get(0);
				String type = typeRel.get(field).get(0);

				String vis = "";
				if (visRel.containsKey(field))
					vis = visRel.get(field).get(0);

				VariableDeclarationFragment fieldId = ast
						.newVariableDeclarationFragment();
				fieldId.setName(ast.newSimpleName(id.toLowerCase()));

				FieldDeclaration fieldDeclaration = ast
						.newFieldDeclaration(fieldId);

				Modifier m = getAccessModifier(vis);
				if (m != null)
					fieldDeclaration.modifiers().add(m);

				Type t = getType(type);
				fieldDeclaration.setType(t);

				// inicializa o field
				VariableDeclarationFragment variable = (VariableDeclarationFragment) fieldDeclaration
						.fragments().get(0);
				Expression inializer;
				if (type.equals("Int__0") || type.equals("Long__0")) {
					// pega o valor que sera inicializado a partir da inst�ncia
					String value = "1" + field.substring(field.length() - 1);
					// Random generator = new Random();
					// int value = generator.nextInt(100);
					inializer = ast.newNumberLiteral(value);
				} else
					inializer = ast.newNullLiteral();
				variable.setInitializer(inializer);

				result.add(fieldDeclaration);
			}
		}

		return result;
	}

	private Type getType(String type) {
		Type result = null;

		// SimpleType newSimpleType = ast.newSimpleType(ast
		// .newName("String");

		if (type.equals("Long__0"))
			result = ast.newPrimitiveType(PrimitiveType.LONG);
		else if (type.equals("Int__0"))
			result = ast.newPrimitiveType(PrimitiveType.INT);

		else {
			Sig classSig = getSig("Class");
			SafeList<Field> classFields = classSig.getFields();
			Field classIdRelations = getField("id", classFields);
			Map<String, List<String>> classRel = getRelations(classIdRelations);
			List<String> id = classRel.get(type);
			if (id != null) {
				result = ast.newSimpleType(ast.newName(id.get(0)));
			}
		}
		return result;
	}

	private Modifier getAccessModifier(String vis) {
		Modifier result = null;

		if (vis.equals("private__0"))
			result = ast.newModifier(Modifier.ModifierKeyword.PRIVATE_KEYWORD);
		else if (vis.equals("protected_0"))
			result = ast
					.newModifier(Modifier.ModifierKeyword.PROTECTED_KEYWORD);
		else if (vis.equals("public_0"))
			result = ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD);
		else if (vis.equals("abstract"))
			result = ast.newModifier(Modifier.ModifierKeyword.ABSTRACT_KEYWORD);
		return result;
	}

	private List<ImportDeclaration> getImports(String classId) {
		List<ImportDeclaration> result = new ArrayList<ImportDeclaration>();

		Sig cuSig = getSig("Class");
		SafeList<Field> cuFields = cuSig.getFields();
		Field packageRelations = getField("imports", cuFields);
		Map<String, List<String>> r = getRelations(packageRelations);
		List<String> imported = r.get(classId);

		if (imported != null) {
			for (String importedPackage : imported) {
				ImportDeclaration importDeclaration = ast
						.newImportDeclaration();
				importDeclaration.setName(ast.newSimpleName(importedPackage));
				importDeclaration.setOnDemand(true);
				result.add(importDeclaration);
			}
		}

		return result;
	}

	private PackageDeclaration getPackage(String classId) {
		PackageDeclaration result = ast.newPackageDeclaration();

		Sig classSig = getSig("Class");
		SafeList<Field> cFields = classSig.getFields();
		Field packageRelations = getField("package", cFields);
		Map<String, List<String>> r = getRelations(packageRelations);

		String packageName = r.get(classId).get(0);

		result.setName(ast.newSimpleName(packageName));

		return result;
	}

	// private List<String> getCompilationUnits() {
	// List<String> result = new ArrayList<String>();
	//
	// Sig sig = getSig("CompilationUnit");
	// List<String> cuInstances = extractInstances(ans.eval(sig).toString());
	// result.addAll(cuInstances);
	//
	// return result;
	// }

	private Field getField(String key, SafeList<Field> cuFields) {
		Field result = null;

		for (Field field : cuFields) {
			if (field.toString().contains(key)) {
				result = field;
				break;
			}

		}

		return result;
	}

	private Map<String, List<String>> getRelations(Field f) {
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		if (f == null)
			return result;

		String relations = ans.eval(f).toString();
		relations = cleanName(relations);

		if (relations.length() > 0) {
			String[] arrayRelation = relations.split(",");

			for (String relation : arrayRelation) {
				String[] r = relation.split("->");
				r[0] = r[0].replaceAll("javametamodel(.)*/", "");
				r[1] = r[1].replaceAll("javametamodel(.)*/", "");

				if (!result.containsKey(r[0])) {
					ArrayList<String> values = new ArrayList<String>();

					values.add(r[1]);
					result.put(r[0], values);
				} else {
					result.get(r[0]).add(r[1]);
				}
			}

		}

		return result;
	}

	private Sig getSig(String s) {
		Sig result = null;

		SafeList<Sig> sigs = ans.getAllReachableSigs();

		for (Sig sig : sigs) {
			String sigName = removeCrap(sig.toString());
			if (sigName.equals(s)) {
				result = sig;
				break;
			}
		}
		return result;
	}

	private String removeCrap(String instance) {
		instance = instance.replaceAll("[^/]*/", "");
		return instance;
	}

	private List<String> extractInstances(String labels) {
		List<String> result = new ArrayList<String>();

		String instances = cleanName(labels);

		if (instances.length() > 0) {

			String[] types = instances.split(",");
			char empty = ' ';
			for (String typeName : types) {
				if (typeName.charAt(0) == empty)
					typeName = typeName.substring(1);
				typeName = typeName.replaceAll("javametamodel(.)*/", "");
				result.add(typeName);

			}
		}
		return result;
	}

	public String cleanName(String name) {
		String removeBraces = name.substring(1, name.length() - 1);
		String replaceDollar = removeBraces.replace("$", "_");
		String removeSpaces = replaceDollar.replaceAll(" ", "");
		return removeSpaces;
	}

}