package cdolly.model.inspector;

import cdolly.model.Entity;
import cdolly.model.Type;

public class StringCheckerImpl implements IStringChecker {

	private StringCheckerImpl() {
	}

	private static class InstanceHolder {
		static final IStringChecker INSTANCE = new StringCheckerImpl();
	}

	static IStringChecker getInstance() {
		return InstanceHolder.INSTANCE;
	}

	@Override
	public boolean isGlobalVariableDeclaration(String declaration) {
		return Entity.GLOBAL_VARIABLE.isDeclaredBy(declaration);
	}

	@Override
	public boolean isFunctionDeclaration(String declaration) {
		return Entity.FUNCTION.isDeclaredBy(declaration);
	}

	@Override
	public boolean isAnIntegerType(String rawType) {
		return Type.INTEGER.isCompatibleBy(rawType);
	}

	@Override
	public boolean isAFloatType(String rawType) {
		return Type.FLOAT.isCompatibleBy(rawType);
	}

	@Deprecated
	@Override
	public boolean isADoubleType(String rawType) {
		throw new UnsupportedOperationException("method not implemented");
	}

	@Deprecated
	@Override
	public boolean isACharType(String rawType) {
		throw new UnsupportedOperationException("method not implemented");
	}

	@Deprecated
	@Override
	public boolean isALiteralReturn(String rawReturnStmt) {
		throw new UnsupportedOperationException("method not implemented");
	}

	@Override
	public boolean isLocalVariableDeclaration(String declaration) {
		return Entity.LOCAL_VAR_DECL.isDeclaredBy(declaration);
	}

	@Override
	public boolean isIfDefDeclaration(String declaration) {
		return Entity.IF_DEF.isDeclaredBy(declaration);
	}

	@Override
	public boolean isReturnStatement(String declaration) {
		return Entity.RETURN.isDeclaredBy(declaration);
	}

	@Override
	public boolean isVariableReturn(String rawReturnStmt) {
		return isGlobalVariableDeclaration(rawReturnStmt)
				|| Entity.LOCAL_VARIABLE.isDeclaredBy(rawReturnStmt);
	}

	@Override
	public boolean isVarAttribStatement(String declaration) {
		return Entity.VAR_ATTRIB.isDeclaredBy(declaration);
	}

	@Override
	public boolean isAPointerType(String rawType) {
		// TODO Auto-generated method stub
		return Type.POINTER.isCompatibleBy(rawType);
	}

}
