package cdolly.model;

import org.apache.log4j.Logger;

import cdolly.synthesis.SynthesisUnit;

/**
 * @author Jeanderson Candido<br>
 *         <a href="http://jeandersonbc.github.io"
 *         target="_blank">http://jeandersonbc.github.io</a>
 */
public enum Entity {
	

	TRANSLATION_UNIT("TranslationUnit"), FUNCTION("Function"), GLOBAL_VARIABLE(
			"GlobalVar"), LOCAL_VARIABLE("LocalVar"), DECLARATION(
			"Declaration"), IDENTIFIER("Identifier"), RETURN_STMT("ReturnStmt"), STATEMENT("Stmt"),
			RETURN("Return"), LOCAL_VAR_DECL("LocalVarDecl"), IF_DEF("ifDef"), DEFINE("Define"),
			VARIABLE("Variable"), VAR_ATTRIB("VarAttrib");

	private String label;

	private static Logger logger = Logger.getLogger(Entity.class);
	
	Entity(String sigLabel) {
		this.label = sigLabel;
	}

	@Override
	public String toString() {
		return this.label;
	}

	/**
	 * 
	 * @return The label for this {@link Entity}.
	 */
	public String label() {
		return toString();
	}

	public boolean isDeclaredBy(String declaration) {
		logger.debug("DECLARATION: " + declaration + " label: " + label);
		return (declaration == null ? false : declaration.contains(this.label));
	}
}
