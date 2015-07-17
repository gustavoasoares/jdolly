package cdolly.synthesis;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.cdt.core.dom.ast.IASTCompoundStatement;
import org.eclipse.cdt.core.dom.ast.IASTDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTInitializer;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTParameterDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorIncludeStatement;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.IBasicType.Kind;
import org.eclipse.cdt.core.dom.ast.IType;
import org.eclipse.cdt.core.dom.ast.c.ICNodeFactory;
import org.eclipse.cdt.core.dom.rewrite.DeclarationGenerator;
import org.eclipse.cdt.internal.core.dom.parser.c.CASTDeclarator;
import org.eclipse.cdt.internal.core.dom.parser.c.CASTFunctionDeclarator;
import org.eclipse.cdt.internal.core.dom.parser.c.CASTLiteralExpression;
import org.eclipse.cdt.internal.core.dom.parser.c.CASTName;
import org.eclipse.cdt.internal.core.dom.parser.c.CASTSimpleDeclSpecifier;
import org.eclipse.cdt.internal.core.dom.parser.c.CASTSimpleDeclaration;
import org.eclipse.cdt.internal.core.dom.parser.c.CBasicType;
import org.eclipse.cdt.internal.core.dom.parser.c.CNodeFactory;

import cdolly.model.inspector.AlloyInspector;
import cdolly.model.inspector.IModelVisitor;
import cdolly.model.inspector.IStringChecker;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;

/**
 * @author Jeanderson Candido<br>
 *         <a target="_blank"
 *         href="http://jeandersonbc.github.io">http://jeandersonbc
 *         .github.io</a>
 * 
 */
public class SynthesisUnit_OLD {

	private static Logger logger = Logger.getLogger(SynthesisUnit_OLD.class);

	/**
	 * Factory for C entities
	 */
	private static ICNodeFactory nodeFactory = CNodeFactory.getDefault();
	private static DeclarationGenerator generator = DeclarationGenerator
			.create(nodeFactory);

	private static IModelVisitor inspector;

	/**
	 * Extracts the C code specified by an Alloy model
	 * 
	 * @param alloyInstance
	 *            the alloy specification for the code to be extracted.
	 * @return A list of Translation Units ({@link IASTTranslationUnit}).
	 */
	public static List<IASTTranslationUnit> extractCode(A4Solution alloyInstance) {
		List<IASTTranslationUnit> sources = new ArrayList<IASTTranslationUnit>();
		if (alloyInstance == null) {
			logger.warn("Alloy instance is a null reference");
			return sources;
		}
		inspector = new AlloyInspector(alloyInstance);
		// for (String translationUnit : inspector.getTranslationUnits()) {
		sources.add(synthetizeUnit());
		// }
		return sources;
	}

	private static IASTTranslationUnit synthetizeUnit() {
		IASTTranslationUnit source = nodeFactory.newTranslationUnit(null);

		List<String> functions = inspector.getFunctions();
		List<String> globalVariables = inspector.getGlobalVariables();
		List<String> defines = inspector.getDefines();

		for (String globalVariable : globalVariables) {
			source.addDeclaration(synthetizeVariableFrom(globalVariable, true));
		}

		for (String define : defines) {
			source.addDeclaration(synthetizeDefineFrom(define));
		}

		for (String function : functions) {
			source.addDeclaration(synthetizeFunctionFrom(function));
		}

		return source;
	}

	private static IASTDeclaration synthetizeVariableFrom(String declaration,
			boolean isGlobal) {
		// pega-se assim pois nao tem, na teoria atual, id dentro da sig
		String name = inspector.getRawName(declaration);

		IType type = extractVariableTypeFrom(declaration, isGlobal);
		IASTDeclarator declarator = generator.createDeclaratorFromType(type,
				name.toCharArray());

		IASTDeclSpecifier specifier = generator.createDeclSpecFromType(type);
		IASTSimpleDeclaration varDeclaration = nodeFactory
				.newSimpleDeclaration(specifier);

		IASTInitializer initializer = createLiteralFrom(type);
		declarator.setInitializer(initializer);
		varDeclaration.addDeclarator(declarator);

		return varDeclaration;
	}

	private static IASTDeclaration synthetizeFunctionFrom(String declaration) {
		IASTSimpleDeclSpecifier returnType = extractReturnTypeFrom(declaration);
		CASTFunctionDeclarator functionDecl = extractFunctionDeclaratorFrom(declaration);
		IASTCompoundStatement body = nodeFactory.newCompoundStatement();

		// statements
		IStringChecker checker = inspector.getStringChecker();
		for (String statement : inspector.getStatementsFrom(declaration)) {
			logger.debug("Statement: " + statement + " Declaration: "
					+ declaration);
			if (checker.isIfDefDeclaration(statement)) {
				IASTDeclaration defineDeclaration = synthetizeIfDefFrom(statement); 
			} else if (checker.isLocalVariableDeclaration(statement)) {
				// TODO Parei Aqui. Alterar linha abaixo, pois o que esta sendo
				// passado nao a um
				// LocalVar, e sim um LocalVarDecl, ou seja, tem uma indirecao
				IASTDeclaration variable = synthetizeLocalVariableDeclFrom(statement);
				body.addStatement(nodeFactory.newDeclarationStatement(variable));

			} else if (checker.isReturnStatement(statement)) {
				IASTExpression returnStatement = extractReturnStmtFrom(
						declaration, returnType);
				body.addStatement(nodeFactory
						.newReturnStatement(returnStatement));
			}
		}
		IASTFunctionDefinition function = nodeFactory.newFunctionDefinition(
				returnType, functionDecl, body);

		return function;
	}

	private static IASTDeclaration synthetizeIfDefFrom(String statement) {
		// TODO Auto-generated method stub
		return null;
	}

	private static IASTDeclaration synthetizeLocalVariableDeclFrom(
			String statement) {

		String name = inspector.getRawName(statement);
		String variableType = inspector.getVariableDeclTypeFrom(statement);

		IType type = extractVariableTypeFrom(variableType, false);
		IASTDeclarator declarator = generator.createDeclaratorFromType(type,
				name.toCharArray());

		IASTDeclSpecifier specifier = generator.createDeclSpecFromType(type);
		IASTSimpleDeclaration varDeclaration = nodeFactory
				.newSimpleDeclaration(specifier);

		IASTInitializer initializer = createLiteralFrom(type);
		declarator.setInitializer(initializer);
		varDeclaration.addDeclarator(declarator);

		return varDeclaration;
	}

	private static IASTExpression extractReturnStmtFrom(String declaration,
			IASTSimpleDeclSpecifier returnType) {
		IStringChecker checker = inspector.getStringChecker();
		String rawReturnStmt = inspector.getReturnStmtFrom(declaration);
		if (checker.isVariableReturn(rawReturnStmt)) {
			return createReturnLocalVarFor(rawReturnStmt);
		}
		throw new UnknownReturnStmtException(rawReturnStmt);
	}

	private static IASTExpression createReturnLocalVarFor(String rawReturnStmt) {
		IASTName varName = nodeFactory.newName(inspector.getRawName(
				rawReturnStmt).toCharArray());

		return nodeFactory.newIdExpression(varName);
	}

	@Deprecated
	private static IASTExpression createReturnLiteralFor(
			IASTSimpleDeclSpecifier returnType) {

		IASTExpression returnExpr = null;
		if (returnType.getType() == IASTSimpleDeclSpecifier.t_int) {
			returnExpr = nodeFactory.newLiteralExpression(returnType.getType(),
					"1");
		} else if (returnType.getType() == IASTSimpleDeclSpecifier.t_char) {
			returnExpr = nodeFactory.newLiteralExpression(returnType.getType(),
					"'a'");
		} else if (returnType.getType() == IASTSimpleDeclSpecifier.t_double) {
			returnExpr = nodeFactory.newLiteralExpression(returnType.getType(),
					"3");
		} else if (returnType.getType() == IASTSimpleDeclSpecifier.t_float) {
			returnExpr = nodeFactory.newLiteralExpression(returnType.getType(),
					"2");
		}
		return returnExpr;
	}

	private static IASTSimpleDeclSpecifier extractReturnTypeFrom(
			String declaration) {
		int type = IASTSimpleDeclSpecifier.t_void;

		String rawType = inspector.getReturnTypeFrom(declaration);
		IStringChecker checker = inspector.getStringChecker();
		if (checker.isAnIntegerType(rawType)) {
			type = IASTSimpleDeclSpecifier.t_int;

		} else if (checker.isAFloatType(rawType)) {
			type = IASTSimpleDeclSpecifier.t_float;
		}
		return createTypeBasedOn(type);
	}

	private static IType extractVariableTypeFrom(String declaration,
			boolean isGlobal) {

		String rawType = inspector.getDeclaringTypeFrom(declaration, isGlobal);
		logger.debug("ANTES rawType: " + declaration + " rawType" + rawType
				+ " isGlobal " + isGlobal);
		IStringChecker checker = inspector.getStringChecker();
		if (checker.isAnIntegerType(rawType)) {
			return new CBasicType(Kind.eInt, 0);

		} else if (checker.isAFloatType(rawType)) {
			return new CBasicType(Kind.eFloat, 0);
		}
		throw new UnknownRawTypeException(rawType);
	}

	private static IASTInitializer createLiteralFrom(IType type) {
		char[] value = null;
		int kind = 0;
		if (type != null) {
			if (type.isSameType(new CBasicType(Kind.eInt, 0))) {
				kind = IASTSimpleDeclSpecifier.t_int;
				value = "1".toCharArray();

			} else if (type.isSameType(new CBasicType(Kind.eDouble, 0))) {
				kind = IASTSimpleDeclSpecifier.t_double;
				value = "2".toCharArray();

			} else if (type.isSameType(new CBasicType(Kind.eFloat, 0))) {
				kind = IASTSimpleDeclSpecifier.t_float;
				value = "3".toCharArray();

			} else if (type.isSameType(new CBasicType(Kind.eChar, 0))) {
				kind = IASTSimpleDeclSpecifier.t_char;
				value = "'a'".toCharArray();
			}
		}
		return nodeFactory.newEqualsInitializer(new CASTLiteralExpression(kind,
				value));
	}

	private static CASTFunctionDeclarator extractFunctionDeclaratorFrom(
			String declaration) {
		String rawFunctionName = inspector.getRawName(declaration);
		IASTName functionName = nodeFactory.newName(rawFunctionName
				.toCharArray());

		CASTFunctionDeclarator functionDecl = new CASTFunctionDeclarator(
				functionName);

		List<IASTParameterDeclaration> params = extractParamaters(declaration);
		for (IASTParameterDeclaration param : params) {
			functionDecl.addParameterDeclaration(param);
		}
		return functionDecl;
	}

	private static List<IASTParameterDeclaration> extractParamaters(
			String declaration) {
		List<IASTParameterDeclaration> params = new LinkedList<IASTParameterDeclaration>();
		List<String> rawParams = inspector.getParametersFrom(declaration);

		IASTDeclSpecifier type = null;
		char[] realName = null;
		for (String rawParam : rawParams) {
			realName = inspector.getRawName(rawParam).toCharArray();
			logger.debug("rawParam ++ RealName : " + rawParam + " ++ "
					+ realName);

			type = generator.createDeclSpecFromType(extractVariableTypeFrom(
					rawParam, false));

			params.add(nodeFactory.newParameterDeclaration(type,
					new CASTDeclarator(new CASTName(realName))));

		}
		return params;
	}

	private static IASTSimpleDeclSpecifier createTypeBasedOn(int type) {
		IASTSimpleDeclSpecifier typeSpecifier = new CASTSimpleDeclSpecifier();
		typeSpecifier.setType(type);

		return typeSpecifier;
	}

	private static IASTDeclaration synthetizeDefineFrom(String declaration) {
		String name = inspector.getRawName(declaration);
		//TODO retornar aqui uma declaração de #DEFINE name
		return null;
	}
}
