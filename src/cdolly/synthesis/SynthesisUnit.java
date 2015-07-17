package cdolly.synthesis;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;

import cdolly.model.CFile;
import cdolly.model.Define;
import cdolly.model.Entity;
import cdolly.model.Function;
import cdolly.model.IfDefStatement;
import cdolly.model.RandomValueGenerator;
import cdolly.model.ReturnStatement;
import cdolly.model.Statement;
import cdolly.model.VarAttribStatement;
import cdolly.model.VarDeclarationStatement;
import cdolly.model.Variable;
import cdolly.model.VariableType;
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
public class SynthesisUnit {

	private static Logger logger = Logger.getLogger(SynthesisUnit.class);


	private static IModelVisitor inspector;

	/**
	 * Extracts the C code specified by an Alloy model
	 * 
	 * @param alloyInstance
	 *            the alloy specification for the code to be extracted.
	 * @return A list of Translation Units ({@link IASTTranslationUnit}).
	 */
	public static List<CFile> extractCode(A4Solution alloyInstance) {
		List<CFile> sources = new ArrayList<CFile>();
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

	private static CFile synthetizeUnit() {
		CFile source = new CFile();
		
		List<String> functions = inspector.getFunctions();
		List<String> globalVariables = inspector.getGlobalVariables();
		List<String> defines = inspector.getDefines();

		for (String globalVariable : globalVariables) {
			source.addGlobalVariable(synthetizeVariableFrom(globalVariable, true));
		}

		for (String define : defines) {
			source.addDefine(synthetizeDefineFrom(define));
		}

		for (String function : functions) {
			source.addFunction(synthetizeFunctionFrom(function));
		}

		return source;
	}

	private static VarDeclarationStatement synthetizeVariableFrom(String declaration,
			boolean isGlobal) {
		
		
		// pega-se assim pois nao tem, na teoria atual, id dentro da sig
		String name = inspector.getRawName(declaration);

		VariableType type = extractVariableTypeFrom(declaration, isGlobal);
		Variable variable = new Variable(type, name);
		
		String value = createLiteralFrom(type);
		VarDeclarationStatement varDeclaration = new VarDeclarationStatement(variable, value);
		return varDeclaration;
	}

	private static Function synthetizeFunctionFrom(String declaration) {
		VariableType returnType = extractReturnTypeFrom(declaration);
		String rawFunctionName = inspector.getRawName(declaration);
		Function function = new Function(returnType, rawFunctionName);
		
		logger.debug("#########################DECLARATION: " + declaration);
		//params
        function.setParams(extractParamaters(declaration));
        
		// statements
		IStringChecker checker = inspector.getStringChecker();
		for (String statement : inspector.getStatementsFrom(declaration)) {
			Statement statementDecl = null;
			logger.debug("#########################Statement: " + statement);
			if (inspector.isStatementOfEntity(Entity.IF_DEF, statement)) {
				logger.debug("IS IF_DEF");
				statementDecl = synthetizeIfDefFrom(statement);
			} else if (inspector.isStatementOfEntity(Entity.LOCAL_VAR_DECL, statement)) {
				logger.debug("IS LOCAL_VAR_DECL");
				statementDecl = synthetizeLocalVariableDeclFrom(statement);
			} else if (inspector.isStatementOfEntity(Entity.RETURN, statement)) {
				logger.debug("IS RETURN");
				statementDecl = extractReturnStmtFrom(statement);
			} else if (inspector.isStatementOfEntity(Entity.VAR_ATTRIB, statement)) {
				logger.debug("IS VAR_ATTRIB. Statement: " + statement);
				statementDecl = synthetizeVariableAttributionFrom(statement);
			}
			logger.debug("SOURCE: " + statementDecl.getSource());
			logger.debug(" Declaration: "
					+ declaration + (statementDecl==null?" STATEMENT SER� NULL!":""));

			function.addStatement(statementDecl);
		}
	
		return function;
	}

	private static IfDefStatement synthetizeIfDefFrom(String statement) {
		
		String def = inspector.getRawName(inspector.getDefFrom(statement));
		String cmdField = inspector.getCmdFrom(statement);
		logger.debug("COMMAND do IFDEF: " + cmdField);
		VarAttribStatement varCmdStmt= synthetizeVariableAttributionFrom(cmdField);

		String elseField = inspector.getElseFrom(statement);
		VarAttribStatement varElseStmt = null;
		if (elseField != null && !"".equals(elseField)){
			varElseStmt = synthetizeVariableAttributionFrom(elseField);
		}

		IfDefStatement ifDef = new IfDefStatement(def, varCmdStmt, varElseStmt);
		return ifDef;
	
	}

	private static VarDeclarationStatement synthetizeLocalVariableDeclFrom(
			String statement) {
		String name = inspector.getRawName(inspector.getLocalVarDecVFrom(statement));
		String strType = inspector.getVariableDeclTypeFrom(statement);

		VariableType type = extractVariableTypeFrom(strType, false);
		Variable variable = new Variable(type, name);

		String initializer = createLiteralFrom(type);
		
		VarDeclarationStatement varDecStmt = new VarDeclarationStatement(variable, initializer);

		return varDecStmt;
	}
	
	private static VarAttribStatement synthetizeVariableAttributionFrom(
			String statement) {

		String name = inspector.getVariableNameFrom(statement, false);
		String nameAtt = inspector.getAttVariableNameFrom(statement, false);
		
		String strType = inspector.getVariableAttribTypeFrom(statement);

		VariableType type = extractVariableTypeFrom(strType, false);
		Variable variable = new Variable(type, name);
		
		Variable variableAtt; 
		VarAttribStatement varAttribStmt = null;
		
		if (!nameAtt.equals("")) {
			variableAtt = new Variable(type, nameAtt);
			varAttribStmt = new VarAttribStatement(variable, variableAtt);
		} else {
//			System.out.println("PASSOOOUU=====================================");
			String initializer = createLiteralFrom(type);
			varAttribStmt = new VarAttribStatement(variable, initializer);
		}

		return varAttribStmt;
	}
	
	private static Statement extractVarAttribStmtFrom(String declaration) {
		String rawVarAttribStmt = inspector.getRawName(inspector.getVFrom(declaration));
		VariableType vType = extractReturnTypeFrom(declaration);
		String value = createLiteralFrom(vType);
		Variable variable = new Variable(vType, rawVarAttribStmt);
		VarAttribStatement varAttrib = new VarAttribStatement(variable, value);
		return varAttrib;
	}


	private static ReturnStatement extractReturnStmtFrom(String declaration) {
		String returnStmt = inspector.getReturnStmtFrom(declaration);
		logger.debug("Return Statement: " + returnStmt + " Declaration: " + declaration);
		if (inspector.isStatementOfEntity(Entity.RETURN, declaration)) {
			return new ReturnStatement(inspector.getRawName(returnStmt));
		}
		throw new UnknownReturnStmtException(returnStmt);
	}

	private static VariableType extractReturnTypeFrom(
			String declaration) {

		VariableType type = VariableType.voidd;
		String rawType = inspector.getReturnTypeFrom(declaration);
		IStringChecker checker = inspector.getStringChecker();
		if (checker.isAnIntegerType(rawType)) {
			type = VariableType.intt;
		} else if (checker.isAFloatType(rawType)) {
			type = VariableType.floatt;
		}
		return type;
	}

	private static VariableType extractVariableTypeFrom(String declaration,
			boolean isGlobal) {

		String rawType = inspector.getDeclaringTypeFrom(declaration, isGlobal);
		logger.debug("ANTES rawType: " + declaration + " rawType" + rawType
				+ " isGlobal " + isGlobal);
		IStringChecker checker = inspector.getStringChecker();
		if (checker.isAnIntegerType(rawType)) {
			return VariableType.intt;

		} else if (checker.isAFloatType(rawType)) {
			return VariableType.floatt;
		}
		
		else if (checker.isAPointerType(rawType)) {
			return VariableType.pointer;
		}
		throw new UnknownRawTypeException(rawType);
	}

	private static String createLiteralFrom(VariableType type) {
		String value = null;
		if (type != null) {
			if (type == VariableType.intt) {
				value = RandomValueGenerator.getInt();
			} else if (type == VariableType.doublee) {
				value = RandomValueGenerator.getFloat();
			} else if (type == VariableType.floatt) {
				value = RandomValueGenerator.getFloat();
			} else if (type == VariableType.charr) {
				value = RandomValueGenerator.getChar();
			} 
		}
		return value;
	}

	private static List<Variable> extractParamaters(
			String declaration) {
		List<Variable> params = new LinkedList<Variable>();
		List<String> rawParams = inspector.getParametersFrom(declaration);

		String realName = null;
		for (String rawParam : rawParams) {
			realName = inspector.getRawName(rawParam);
			logger.debug("rawParam ++ RealName : " + rawParam + " ++ "
					+ realName);
			VariableType type = extractVariableTypeFrom(rawParam, false);
			params.add(new Variable(type, realName));

		}
		return params;
	}

	private static Define synthetizeDefineFrom(String declaration) {
		String name = inspector.getRawName(declaration);
		return new Define(name);
	}
}
