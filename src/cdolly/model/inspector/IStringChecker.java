package cdolly.model.inspector;

public interface IStringChecker {

	boolean isGlobalVariableDeclaration(String rawDeclaration);

	boolean isFunctionDeclaration(String rawDeclaration);

	boolean isAnIntegerType(String rawType);
	
	boolean isAPointerType(String rawType);

	boolean isAFloatType(String rawType);

	@Deprecated
	boolean isADoubleType(String rawType);

	@Deprecated
	boolean isACharType(String rawType);

	@Deprecated
	boolean isALiteralReturn(String rawReturnStmt);

	boolean isVariableReturn(String rawReturnStmt);

	boolean isLocalVariableDeclaration(String declaration);

	boolean isIfDefDeclaration(String declaration);

	boolean isReturnStatement(String declaration);
	
	boolean isVarAttribStatement(String declaration);
}
