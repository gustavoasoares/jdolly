package jdolly;

import edu.mit.csail.sdg.alloy4.SafeList;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig.Field;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import jdolly.visitors.ImportCheckerVisitor;
import jdolly.visitors.ImportVisitor;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;

import java.util.*;

public class AlloyToJavaTranslator {

	// private final String ALLOY_MODULE_NAME = "javametamodel";
	private static AST ast = AST.newAST(AST.JLS3);
	private A4Solution ans;

	public AlloyToJavaTranslator(A4Solution ans) {
		this.ans = ans;
	}

	public List<CompilationUnit> getJavaCode() {

		List<CompilationUnit> generatedPrograms = getCompilationUnits();

		ImportVisitor cv = new ImportVisitor();
		PackageDeclaration compilationUnitPackage;
		Name packageName;

		// colocar imports
		for (CompilationUnit cu : generatedPrograms) {
			cu.accept(cv);
			compilationUnitPackage = cu.getPackage();
			packageName = compilationUnitPackage.getName();

			List<String> importedPackages = new ArrayList<String>();

			for (CompilationUnit cu2 : generatedPrograms) {
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
		return generatedPrograms;
	}

	private List<CompilationUnit> getCompilationUnits() {
		List<CompilationUnit> generatedPrograms = new ArrayList<CompilationUnit>();

		CompilationUnit compilationUnit;
		PackageDeclaration packageDeclaration;
		TypeDeclaration typeDeclaration;

		final List<String> classesInstances = getClassesInstances();
		for (String className : classesInstances) {
			compilationUnit = ast.newCompilationUnit();
			packageDeclaration = getClassPackage(className);
			typeDeclaration = getClassBody(className);

			compilationUnit.setPackage(packageDeclaration);

			compilationUnit.types().add(typeDeclaration);

			generatedPrograms.add(compilationUnit);
		}
		return generatedPrograms;
	}

	private ImportDeclaration importPacakge(Name packageToCompare) {
		ImportDeclaration importDeclaration = ast.newImportDeclaration();
		importDeclaration.setName(ast.newSimpleName(packageToCompare
				.getFullyQualifiedName()));
		importDeclaration.setOnDemand(true);
		return importDeclaration;
	}

	private List<String> getClassesInstances() {
		List<String> result = new ArrayList<String>();

		Sig sig = getSig("Class");
		List<String> classInstances = AlloyToJavaUtil.extractInstances(ans.eval(sig).toString());
		result.addAll(classInstances);

		return result;
	}

	/**Return the body of the class (definition, fields and methods)
	 * related to the given classId.
	 *
	 * An example of output would be:
		 public class ClassId_0 {
			public long methodid_0() {
				return fieldid_1;
			}
			public int fieldid_1=10;
		 }
	 * */
	private TypeDeclaration getClassBody(String classId) {
		TypeDeclaration classBody = ast.newTypeDeclaration();

		Sig classSig = getSig("Class");
		SafeList<Field> classFields = classSig.getFields();
		
		Map<String, List<String>> classIdRel = getClassRelationsBy("id");
		Map<String, List<String>> extendRel = getClassRelationsBy("extend");
		
		Field classIsInterfaceRel = getClassFieldsByCriteria("isInterface");
		Field classIsAbstractRel = getClassFieldsByCriteria("isAbstract");
		
		//MOD1
		Field classImplementRel = getClassFieldsByCriteria("implement");
		if (classImplementRel != null) {
			Map<String, List<String>> implementRel = getClassRelationsBy("implement");

			//MOD2
			List<String> interface_ = implementRel.get(classId);
			if (interface_ != null) {
				String interfaceName = classIdRel.get(interface_.get(0)).get(0);
				classBody.superInterfaceTypes().add(ast.newSimpleType(ast
						.newSimpleName(interfaceName)));
			}
		}
		//isInterface -> extract method: checkIfClassInterfRelIsNull
		if (classIsInterfaceRel != null) {
			Map<String, List<String>> IsInterfacRel = getMethodsRelationsBy("isInterface");
			String isInterface = IsInterfacRel.get(classId).get(0);
			boolean isInterface_ = isInterface.contains("True");
			if (isInterface_) {
				classBody.setInterface(true);
			}
			// methods
			List<MethodDeclaration> methods = getClassMethods(classFields, classId, isInterface_);
			addMethodsTo(methods, classBody);
		} else {
			// methods
			List<MethodDeclaration> methods = getClassMethods(classFields, classId,false);
			addMethodsTo(methods, classBody);
		}

		//isInterface
		if (classIsAbstractRel != null) {
			Map<String, List<String>> IsAbstractRel = getMethodsRelationsBy("isAbstract");
			String isAbstract = IsAbstractRel.get(classId).get(0);
			boolean isAbstract_ = isAbstract.contains("True");
			if (isAbstract_) {
				Modifier abstractModifier = getModifier("abstract");
				addModifierTo(abstractModifier, classBody);
			}
		}
	
		// id
		String className = classIdRel.get(classId).get(0);
		classBody.setName(ast.newSimpleName(className));

		// visibilidade publica
		Modifier publicModifier = getModifier("public");
		addModifierTo(publicModifier, classBody);

		// heran�a
		List<String> superClass = extendRel.get(classId);
		if (superClass != null) {
			String superClassName = classIdRel.get(superClass.get(0)).get(0);
			classBody.setSuperclassType(ast.newSimpleType(ast
					.newSimpleName(superClassName)));
		}

		// atributos
		List<FieldDeclaration> fields = getClassFields(classFields, classId);
		addFieldsTo(fields, classBody);

		return classBody;
	}

	private Modifier getModifier(final String modifierName) {
		return ast.newModifier(getModifierByName(modifierName));
	}

	private ModifierKeyword getModifierByName(final String modifierName) {
		return ModifierKeyword.toKeyword(modifierName);
	}

	private void addModifierTo(final Modifier modifier, TypeDeclaration classBody) {
		classBody.modifiers().add(modifier);
	}

	private void addMethodsTo(final List<MethodDeclaration> methods, TypeDeclaration classBody) {
		for (MethodDeclaration methodDeclaration : methods) {
            classBody.bodyDeclarations().add(methodDeclaration);
        }
	}

	private void addFieldsTo(final List<FieldDeclaration> fields, TypeDeclaration classBody) {
		for (FieldDeclaration fieldDeclaration : fields) {
			classBody.bodyDeclarations().add(fieldDeclaration);
		}
	}

	private List<MethodDeclaration> getClassMethods(
			SafeList<Field> classFields, String classId, boolean isInterface) {
		List<MethodDeclaration> result = new ArrayList<MethodDeclaration>();

		Field methodsRel = AlloyToJavaUtil.getField("methods", classFields);

		Map<String, List<String>> methodsMap = getRelations(methodsRel);

		List<String> methods = methodsMap.get(classId);

		if (methods != null) {
			
			Map<String, List<String>> idRel = getMethodsRelationsBy("id");
			Field mIsAbstractRelations = getMethodsFieldsByCriteria("isAbstract");
			Map<String, List<String>> argRel = getMethodsRelationsBy("param");
			Map<String, List<String>> visRel = getMethodsRelationsBy("acc");
			Map<String, List<String>> bodyRel = getMethodsRelationsBy("b");

			for (String method : methods) {
				String id = idRel.get(method).get(0);
				String arg = AlloyToJavaUtil.setVisValue(argRel, method);

				String vis = AlloyToJavaUtil.setVisValue(visRel, method);

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

	private String getIdFromInvocation(Sig signature, String bodyId, String key){
		SafeList<Field> sFields = signature.getFields();
		Field idRelations = AlloyToJavaUtil.getField(key, sFields);

		return getFieldInstance(idRelations, bodyId);
		
	}
	
	private String getQualifier(Sig signature, String bodyId){
		SafeList<Field> sFields = signature.getFields();
		Field qualRelations = AlloyToJavaUtil.getField("q", sFields);

		return getFieldInstance(qualRelations, bodyId);
	}
	
	private Expression getMethodBody(String bodyId, String classId) {

		// String instanceName = bodyId.replaceAll("_[0-9]", "");
		Sig s = null;
		Field idClassRelations = null;
		Expression outputInvocationExp;
		String fieldId;
		String methodId;
		String className;
		String qualifier;
		int posOfClassName = 0;
		
		// pega o nome da classe que contem o metodo para usar no qualified this
		Sig classSig = getSig("Class");
		SafeList<Field> classFields = classSig.getFields();
		Field classIdRelations = AlloyToJavaUtil.getField("id", classFields);
		Map<String, List<String>> classIdRel = getRelations(classIdRelations);
		
		/**to do:apply factory pattern to extract all the conditions from here
		 * 
		 * outputInvocation = Factory.getInvocation(bodyId,invocationName) 
		 *  
		 *  */
		if (isInstanceOfType(bodyId, "MethodInvocation")) {
			s = getSig("MethodInvocation");
			methodId = getIdFromInvocation(s, bodyId, "id");
			qualifier = getQualifier(s, bodyId);
			className = classIdRel.get(classId).get(0);//possible violation of demeter's law
			outputInvocationExp = getMethodInvocationExpression(methodId, qualifier, className);

		} else if (isInstanceOfType(bodyId, "ConstructorMethodInvocation")) {
			s = getSig("ConstructorMethodInvocation");
			methodId = getIdFromInvocation(s, bodyId, "idMethod");
			idClassRelations = AlloyToJavaUtil.getField("idClass", s.getFields());
			className = getFieldInstance(idClassRelations, bodyId);
			outputInvocationExp = getConstructorMethodInvocationExpression(methodId, className);
			
		} else if (isInstanceOfType(bodyId, "FieldInvocation")) {
			s = getSig("FieldInvocation");
			fieldId = getIdFromInvocation(s, bodyId, "id");
			qualifier = getQualifier(s, bodyId);
			className = classIdRel.get(classId).get(0);//possible violation of demeter's law
			outputInvocationExp = getFieldInvocationExpression(fieldId, qualifier, className);

		} else if (isInstanceOfType(bodyId, "ConstructorFieldInvocation")) {
			s = getSig("ConstructorFieldInvocation");
			fieldId = getIdFromInvocation(s, bodyId, "id");
			idClassRelations = AlloyToJavaUtil.getField("idClass", s.getFields());
			className = getFieldInstance(idClassRelations, bodyId);
			outputInvocationExp = getConstructorFieldInvocationExpression(fieldId, className);
			
		} else {
			String value = bodyId.toString();
			value = value.replaceAll("(.)*_", "");
			outputInvocationExp = ast.newNumberLiteral(value);
		}
		return outputInvocationExp;
	}

	// the auxiliary getIdFromInvocation can be used here as well
	private boolean isInstanceOfType(String instance, String type) {
		Sig signature = getSig(type);
		boolean answer = true;
		String emptyString = "";
		
		if (signature == null){
			answer = false;
		}else{
			String methodId = getIdFromInvocation(signature, instance, "id");
			if (methodId.equals(emptyString))
				answer = false;
		}return answer;
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
		if (idRel.size() > 0 && idRel.containsKey(key)) {
			result = idRel.get(key).get(0);
		}
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
		
		String parameterType = defParamType(methodId);

		Expression parameter = initializerParam(parameterType);
		
		return parameter;
	}

	private String defParamType(String methodId) {
		Map<String, List<String>> idRel = getMethodsRelationsBy("id");
		Map<String, List<String>> argRel = getMethodsRelationsBy("param");

		String parameterType = "";
		boolean someMethodHasNotParam = false;
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
					someMethodHasNotParam = true;
				}
			}
		}
		if (someMethodHasNotParam) parameterType = "";
//		System.out.println("=================");
		return parameterType;
	}
	
	private Expression createParameter(String methodId) {

		String parameterType = getParamType(methodId);
		Expression parameter = initializerParam(parameterType);
		
		return parameter;
	}

	private String getParamType(String methodId) {
		Map<String, List<String>> idRel = getMethodsRelationsBy("id");
		Map<String, List<String>> argRel = getMethodsRelationsBy("param");

		String parameterType = "";
		for (String s : idRel.keySet()) {
			boolean isSizeOfRelGreaterThanZero = idRel.get(s).size() > 0;
			String firstRel = idRel.get(s).get(0);
			if (isSizeOfRelGreaterThanZero && firstRel.equals(methodId)) {
				if (argRel.containsKey(s)) {
					parameterType = argRel.get(s).get(0);
					break;
				}
			}
		}
		return parameterType;
	}

	private Expression initializerParam(String parameterType) {
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
				FieldAccess fieldAcc = getNewFieldAccess(fieldName);
				ThisExpression thisExpression = ast.newThisExpression();				
				fieldAcc.setExpression(thisExpression);
				result = fieldAcc;
			}
			else if (qualifier.equals("qthis__0")) {
				FieldAccess fieldAcc = getNewFieldAccess(fieldName);
				ThisExpression thisExpression = ast.newThisExpression();
				thisExpression.setQualifier(ast.newSimpleName(classId));
				fieldAcc.setExpression(thisExpression);
				result = fieldAcc;
			}
		}
		return result;
		
	}

	private FieldAccess getNewFieldAccess(SimpleName fieldName) {
		FieldAccess fieldAcc = ast.newFieldAccess();
		fieldAcc.setName(fieldName);

		return fieldAcc;
	}

	private List<FieldDeclaration> getClassFields(SafeList<Field> classFields,
			String td) {
		
		List<FieldDeclaration> result = new ArrayList<FieldDeclaration>();

		Field fieldsRel = AlloyToJavaUtil.getField("fields", classFields);

		Map<String, List<String>> fieldsMap = getRelations(fieldsRel);

		List<String> fields = fieldsMap.get(td);

		if (fields != null) {
			Map<String, List<String>> idRel = getFieldsRelationsBy("id");
			Map<String, List<String>> typeRel = getFieldsRelationsBy("type");
			Map<String, List<String>> visRel = getFieldsRelationsBy("acc");

			for (String field : fields) {
				String id = idRel.get(field).get(0);
				String type = typeRel.get(field).get(0);
				String vis = AlloyToJavaUtil.setVisValue(visRel, field);

				FieldDeclaration fieldDeclaration = getFieldDeclaration(id);

				Modifier m = getAccessModifier(vis);
				if (m != null)
					fieldDeclaration.modifiers().add(m);

				Type t = getType(type);
				fieldDeclaration.setType(t);

				// inicializa o field
				VariableDeclarationFragment variable = (VariableDeclarationFragment) fieldDeclaration
						.fragments().get(0);
				
				Expression inializer = getInitializer(type,field);
				variable.setInitializer(inializer);

				result.add(fieldDeclaration);
			}
		}
		return result;
	}

	private Expression getInitializer(String type, String field) {
		Expression inializer;
		if (type.equals("Int__0") || type.equals("Long__0")) {
			// pega o valor que sera inicializado a partir da inst�ncia
			String value = "1" + field.substring(field.length() - 1);
			// Random generator = new Random();
			// int value = generator.nextInt(100);
			inializer = ast.newNumberLiteral(value);
		} else
			inializer = ast.newNullLiteral();
		return inializer;
	}

	private FieldDeclaration getFieldDeclaration(String id) {
		VariableDeclarationFragment fieldId = ast.newVariableDeclarationFragment();
		
		fieldId.setName(ast.newSimpleName(id.toLowerCase()));

		FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(fieldId);
		return fieldDeclaration;
	}

	private Type getType(String type) {
		Type result = null;

		if (type.equals("Long__0"))
			result = ast.newPrimitiveType(PrimitiveType.LONG);

		else if (type.equals("Int__0"))
			result = ast.newPrimitiveType(PrimitiveType.INT);

		else {
			Map<String, List<String>> classRel = getClassRelationsBy("id");
			List<String> id = classRel.get(type);
			if (id != null) {
				String nameToNewType = id.get(0);
				result = ast.newSimpleType(ast.newName(nameToNewType));
			}
		}
		return result;
	}

	private Modifier getAccessModifier(String vis) {
		Modifier result = null;

		if (vis.equals("private__0"))
			result = getModifier("private");
		else if (vis.equals("protected_0"))
			result = getModifier("protected");
		else if (vis.equals("public_0"))
			result = getModifier("public");
		else if (vis.equals("abstract"))
			result = getModifier("abstract");
		return result;
	}

	private List<ImportDeclaration> getImports(String classId) {
		List<ImportDeclaration> result = new ArrayList<ImportDeclaration>();
		
		Map<String, List<String>> relationsByImport = getClassRelationsBy("imports");
		List<String> imported = relationsByImport.get(classId);

		if (imported != null) {
			for (String importedPack : imported) {
				ImportDeclaration importDeclaration = defImportDeclaration(importedPack);
				result.add(importDeclaration);
			}
		}
		return result;
	}

	private ImportDeclaration defImportDeclaration(String importedPackage) {
		ImportDeclaration importDeclaration = ast
				.newImportDeclaration();
		importDeclaration.setName(ast.newSimpleName(importedPackage));
		importDeclaration.setOnDemand(true);
		
		return importDeclaration;
	}

	private Map<String, List<String>> getEntityRelatByCriteria(String criteria, String entity){
		Sig entitySig = getSig(entity);
		SafeList<Field> entityFields = entitySig.getFields();
		Field entityRelatByCriteria = AlloyToJavaUtil.getField(criteria, entityFields);
		
		return getRelations(entityRelatByCriteria);
	}
	
	private Field getEntityFieldsByCriteria(String criteria, String entity){
		Sig entitySig = getSig(entity);
		SafeList<Field> entityFields = entitySig.getFields();
		Field entityFieldsByCriteria = AlloyToJavaUtil.getField(criteria, entityFields);
		return entityFieldsByCriteria;
	}
		
	private Map<String, List<String>> getMethodsRelationsBy(String criteria){		
		return getEntityRelatByCriteria(criteria, "Method");
	}
	
	private Map<String, List<String>> getClassRelationsBy(String criteria){		
		return getEntityRelatByCriteria(criteria, "Class");
	}
	
	private Map<String, List<String>> getFieldsRelationsBy(String criteria){		
		return getEntityRelatByCriteria(criteria, "Field");
	}
	
	private Field getClassFieldsByCriteria(String criteria){
		return getEntityFieldsByCriteria(criteria, "Class");
	}
	
	private Field getMethodsFieldsByCriteria(String criteria){
		return getEntityFieldsByCriteria(criteria, "Method");
	}
	
	private Field getFieldsOfFieldByCriteria(String criteria){
		return getEntityFieldsByCriteria(criteria, "Field");
	}
	
	private PackageDeclaration getClassPackage(String classId) {
		PackageDeclaration result = ast.newPackageDeclaration();

		Map<String, List<String>> relations = getClassRelationsBy("package");
		List<String> classRelations = relations.get(classId);

		String packageName = classRelations.get(0);
		
		result.setName(ast.newSimpleName(packageName));

		return result;
	}

	private Map<String, List<String>> getRelations(Field field) {
		Map<String, List<String>> result = new HashMap<String, List<String>>();

		if (field == null) {
			return result;
		}
		String rawRelations = ans.eval(field).toString();
		String relationsCleaned = AlloyToJavaUtil.cleanName(rawRelations);

		boolean relationsIsNotEmpty = relationsCleaned.length() > 0;	
		if (relationsIsNotEmpty) {
			addRelationsTo(relationsCleaned, result);
		}
		return result;
	}

	private void addRelationsTo(String relationCleaned, Map<String, List<String>> result) {
		String[] relations = {};
		String[] relationsSplitted = relationCleaned.split(",");
		for (String relation : relationsSplitted) {

			relations = AlloyToJavaUtil.cleanMetaModelNaming(relation);
			if (!result.containsKey(relations[0])) {
				List<String> values = new ArrayList<String>();
				values.add(relations[1]);
				result.put(relations[0], values);
			} else {
				List<String> memberValues = result.get(relations[0]);
				memberValues.add(relations[1]);
			}
		}
	}

	private Sig getSig(final String signature) {
		Sig result = null;
		SafeList<Sig> sigs = ans.getAllReachableSigs();

		for (Sig sig : sigs) {
			String sigName = AlloyToJavaUtil.removeCrap(sig.toString());
			if (sigName.equals(signature)) {
				result = sig;
				break;
			}
		}
		return result;
	}
}

	
