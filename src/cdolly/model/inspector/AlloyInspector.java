package cdolly.model.inspector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import cdolly.model.Entity;
import cdolly.model.Relation;
import edu.mit.csail.sdg.alloy4.SafeList;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig.Field;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;

/**
 * @author Jeanderson Candido<br>
 *         <a target="_blank"
 *         href="http://jeandersonbc.github.io">http://jeandersonbc
 *         .github.io</a>
 * 
 */
public class AlloyInspector implements IModelVisitor {

	private static final String CORE_ID = "core/";
	private A4Solution model;
	private static Logger logger = Logger.getLogger(AlloyInspector.class);

	/**
	 * @param toBeInspected
	 *            The Alloy instance ({@link A4Solution}) to be inspected.
	 */
	public AlloyInspector(A4Solution toBeInspected) {
		if (toBeInspected != null) {
			this.model = toBeInspected;
		}
	}
	
	@Override
	/**
	 * Returns all the instances of the give entity
	 */
	public List<String> getInstancesFrom(Entity entity) {
		Sig sig = getSigByEntity(entity);
		List<String> names = new ArrayList<String>();
		if (sig != null) {
			String rawNames = this.model.eval(sig).toString();
			for (String name : rawNames.split(", ")) {
				name = removeBraces(name);
				if (!name.isEmpty())
					names.add(name);
			}
		}
		return names;
	}

	@Override
	//TODO generalizar, usando getInstancesFrom
	public List<String> getTranslationUnits() {
		Sig sig = getSigByEntity(Entity.TRANSLATION_UNIT);
		List<String> names = new ArrayList<String>();
		if (sig != null) {
			String rawNames = this.model.eval(sig).toString();
			for (String name : rawNames.split(", ")) {
				name = removeBraces(name);
				if (!name.isEmpty())
					names.add(name);
			}
		}
		return names;
	}

	@Override
	//TODO generalizar, usando getInstancesFrom
	public List<String> getFunctions() {
		Sig sig = getSigByEntity(Entity.FUNCTION);
		List<String> names = new ArrayList<String>();
		if (sig != null) {
			String rawNames = this.model.eval(sig).toString();
			for (String name : rawNames.split(", ")) {
				name = removeBraces(name);
				if (!name.isEmpty())
					names.add(name);
			}
		}
		return names;
	}

	@Override
	public List<String> getDefines() {
		Sig sig = getSigByEntity(Entity.DEFINE);
		List<String> names = new ArrayList<String>();
		
		List<String> defineIds = getDeclarationsFrom(sig.label, Entity.DEFINE, Relation.DEFS);
		
//		if (sig != null) {
//			String rawNames = this.model.eval(sig).toString();
//			for (String name : rawNames.split(", ")) {
//				name = removeBraces(name);
//				if (!name.isEmpty())
//					names.add(name);
//			}
//		}
//		return names;
		return defineIds;
	}

	@Override
	public List<String> getGlobalVariables() {
		Sig sig = getSigByEntity(Entity.GLOBAL_VARIABLE);
		List<String> names = new ArrayList<String>();
		if (sig != null) {
			String rawNames = this.model.eval(sig).toString();
			for (String name : rawNames.split(", ")) {
				name = removeBraces(name);
				if (!name.isEmpty())
					names.add(name);
			}
		}
		return names;
	}

	@Deprecated
	@Override
	public List<String> getGlobalDeclarationsFrom(String translationUnit) {
		throw new UnsupportedOperationException("method not implemented");
	}

	@Deprecated
	@Override
	public String getFunctionNameFrom(String declaration) {
		throw new UnsupportedOperationException("method not implemented");
	}

	@Override
	//ex.: a$0 -> a_0
	public String getRawName(String declaration) {
		return getName(declaration);
	}

	@Override
	public List<String> getParametersFrom(String functionDeclaration) {
		return getDeclarationsFrom(functionDeclaration, Entity.FUNCTION,
				Relation.PARAM);
	}

	@Override
	public String getReturnTypeFrom(String declaration) {
		return getTypeFrom(declaration, Entity.FUNCTION, Relation.RETURN_TYPE);
	}

	@Deprecated
	@Override
	public List<String> getLocalDeclarationsFrom(String functionDeclaration) {
		throw new UnsupportedOperationException("method not implemented");
	}

	@Override
	public String getDeclaringTypeFrom(String variableDeclaration,
			boolean isGlobal) {
		logger.debug("Variable declaration: " + variableDeclaration);

		return getTypeFrom(variableDeclaration, Entity.VARIABLE, Relation.TYPE);
	}

	@Override
	public IStringChecker getStringChecker() {
		return StringCheckerImpl.getInstance();
	}

	@Override
	public String getVariableNameFrom(String declaration, boolean isGlobal) {
		return getRawName(getVFrom(declaration));
	}
	
	@Override
	public String getAttVariableNameFrom(String declaration, boolean isGlobal) {
		return getRawName(getAttVFrom(declaration));
	}



	@Override
	public String getReturnStmtFrom(String declaration) {
		String returnStmt = "";
		List<String> functionStmts = getDeclarationsFrom(declaration,
				Entity.FUNCTION, Relation.STATEMENT);

		if (functionStmts.size() > 0) {
			String returnDecl = functionStmts.get(functionStmts.size() - 1);
			List<String> returns = getDeclarationsFrom(returnDecl, Entity.RETURN, Relation.RETURN);
			returnStmt = returns.get(0);
			logger.debug("RETURN STATEMENT=====-> " + returnStmt);
		}
		return returnStmt;
	}

	@Override
	public String getVariableDeclTypeFrom(String declaration) {
		List<String> declarations = getDeclarationsFrom(declaration,
				Entity.LOCAL_VAR_DECL, Relation.VARIABLE);
		// so ha um v:LocalVar em LocalVarDecl
		return declarations.get(0);
	}
	
	@Override
	public String getVariableAttribTypeFrom(String declaration) {
		List<String> declarations = getDeclarationsFrom(declaration,
				Entity.VAR_ATTRIB, Relation.VARIABLE);
		// so ha um v:LocalVar em LocalVarDecl
		return declarations.get(0);
	}

	/**
	 * Gets all declarations of <code>targetDeclaration</code> existing in the
	 * <code>belongingEntity</code> with the relationship
	 * <code>relationKind</code>.
	 * <p>
	 * If such declaration or relation does not exist in the belonging entity,
	 * and empty list is returned.
	 * </p>
	 * 
	 * @param targetDeclaration
	 *            The target declaration
	 * @param belongingEntity
	 *            The entity that has the target declaration.
	 * @param relationKind
	 *            The relation in the belonging entity having the target
	 *            declaration.
	 * @return A list of declarations from target declarations.
	 */
	private List<String> getDeclarationsFrom(String targetDeclaration,
			Entity belongingEntity, Relation relationKind) {
		String rawDeclarations = getRawDeclaration(belongingEntity,
				relationKind);
		logger.debug("RAWDECLARATION: " + rawDeclarations);
		targetDeclaration = targetDeclaration.replaceAll("[0-9]+\\->", "");
		List<String> declarations = new LinkedList<String>();
		for (String declaration : rawDeclarations.split(", ")) {
			if (declaration.contains(targetDeclaration)) {
				declarations.add(removePrefix(belongingEntity, declaration));
			}
		}
		return declarations;
	}

	private String getTypeFrom(String typedDeclaration, Entity entity,
			Relation relationType) {
		String rawDeclarations = getRawDeclaration(entity, relationType);
		logger.debug("getTypeFrom -> rawDeclarations: " + rawDeclarations + "  typedDeclaration: " + typedDeclaration);
		String rawType = "";
		for (String name : rawDeclarations.split(", ")) {
			if (name.contains(typedDeclaration)) {
				rawType = removePrefix(entity, name);
				return rawType.replace(CORE_ID, "").replaceAll("\\$[0-9]*", "");
			}
		}
		logger.debug("getTypeFrom -> rawType: " + rawType);
		return rawType;
	}

	private Field getFieldBy(Entity entity, Relation relation) {
		logger.debug("Entity: " + entity.name() + " Relation: " + relation);
		Sig sig = getSigByEntity(entity);
		logger.debug("Sig: " + sig + " normalized sig: "
				+ normalizeSigName(sig));
		SafeList<Field> fields = sig.getFields();
		logger.debug("fields " + fields.size());
		for (Field f : fields) {
			if (f.toString().contains(entity.label())
					&& f.toString().contains(relation.label()))
				return f;
		}
		throw new FieldNotFoundException(entity, relation);
	}

	private String normalizeSigName(Sig sigInstance) {
		String normalizedSigName = "";
		if (sigInstance != null) {
			normalizedSigName = sigInstance.toString().replaceAll("[^/]*/", "");
		}
		return normalizedSigName;
	}

	// Used when a signature does not have an id field. Example: sig
	// Function{stmt: Stmt}
	private String getName(String declaration) {
		return declaration.replace(CORE_ID, "").replace("$", "_").replaceAll("[0-9]+\\->", "");
	}

	private String getRawDeclaration(Entity entity, Relation relation) {
		logger.debug("getRawDeclaration -> Entity e Relation: " + entity + " "
				+ relation);
		Field field = getFieldBy(entity, relation);
		return removeBraces(this.model.eval(field).toString());
	}

	private Sig getSigByEntity(Entity kind) {
		if (kind != null) {
			SafeList<Sig> allSigsFromModel = this.model.getAllReachableSigs();
			String pureSigName = null;
			for (Sig sig : allSigsFromModel) {
				pureSigName = sig.toString().replace(CORE_ID, "");
				if (pureSigName.equals(kind.label())) {
					return sig;
				}
			}
		}
		throw new SigNotFoundException(kind.label());
	}

	private String removePrefix(Entity entity, String rawDeclarations) {
		String finalName = rawDeclarations.replaceAll(CORE_ID, "");
		String prefixName = rawDeclarations.substring(0, rawDeclarations.indexOf("$"));
		logger.debug("removePrefix -> PREFIX: " + prefixName);
//		regex.append("^[A-Za-z]*\\$[0-9]*\\->");

		finalName = rawDeclarations.replace(prefixName, "");
		logger.debug("removePrefix -> Intermediario: " + finalName);
		finalName = finalName.replaceAll("\\$[0-9]*\\->", "");
		logger.debug("removePrefix -> FinalName: " + finalName);
		return finalName;
	}

	private String removeBraces(String name) {
		return (name != null ? name.replace("{", "").replace("}", "") : "");
	}

	@Override
	public List<String> getStatementsFrom(String functionDeclaration) {
		return getDeclarationsFrom(functionDeclaration, Entity.FUNCTION,
				Relation.STATEMENT);
	}

	@Override
	public List<String> getLocalVariablesFrom(String functionDeclaration) {
		return getDeclarationsFrom(functionDeclaration, Entity.FUNCTION,
				Relation.VARIABLE);
	}
	
	@Override
	public String getDefElementFrom(String ifDefStatement) {
		List<String> s = getDeclarationsFrom(ifDefStatement, Entity.IF_DEF,
				Relation.DEFINE);
		if (s.size()>0){
			return s.get(0);
		}
		return "";
	}

	@Override
	public String getCmdFrom(String statement) {
		List<String> s = getDeclarationsFrom(statement, Entity.IF_DEF,
				Relation.CMD);
		if (s.size()>0){
			return s.get(0);
		}
		return "";
	}

	@Override
	public String getElseFrom(String statement) {
		List<String> s = getDeclarationsFrom(statement, Entity.IF_DEF,
				Relation.ELSE);
		if (s.size()>0){
			return s.get(0);
		}
		return "";
	}
	
	@Override
	public String getVFrom(String statement) {
		List<String> s = getDeclarationsFrom(statement, Entity.VAR_ATTRIB,
				Relation.VARIABLE);
		if (s.size()>0){
			return s.get(0);
		}
		return "";
	}
	
	private String getAttVFrom(String statement) {
		List<String> s = getDeclarationsFrom(statement, Entity.VAR_ATTRIB,
				Relation.VARIABLE2);
		if (s.size()>0){
			return s.get(0);
		}
		return "";
	}
	
	@Override
	public String getLocalVarDecVFrom(String statement) {
		List<String> s = getDeclarationsFrom(statement, Entity.LOCAL_VAR_DECL,
				Relation.VARIABLE);
		if (s.size()>0){
			return s.get(0);
		}
		return "";
	}
	
	/**
	 * Return if the given statement if of type of the given entity
	 * @param statement
	 * @return
	 */
	@Override
	public boolean isStatementOfEntity(Entity entity, String statement){
		List<String> instances = getInstancesFrom(entity);
		for (String instance: instances){
			logger.debug("INSTANCE: " + instance + "   STATEMENT: " + statement);
			if (statement.contains(instance)){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getDefFrom(String statement) {
		List<String> s = getDeclarationsFrom(statement, Entity.IF_DEF,
				Relation.DEFINE);
		if (s.size()>0){
			return s.get(0);
		}
		return "";
	}
}
