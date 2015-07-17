package cdolly.model.inspector;

import edu.mit.csail.sdg.alloy4compiler.ast.Sig;

/**
 * This exception indicates that a {@link Sig} is not defined in the Alloy
 * model.
 * 
 * @author Jeanderson Candido<br>
 *         <a href="http://jeandersonbc.github.io"
 *         target="_blank">http://jeandersonbc.github.io</a>
 * 
 */
public class SigNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Indicates that there is no sig in the Alloy model defined by the given
	 * name
	 * 
	 * @param targetName
	 *            The not found target name
	 */
	public SigNotFoundException(String targetName) {
		super("Could not find Sig named \"" + targetName + "\".");
	}

	/**
	 * Indicates that a Sig was not defined in the Alloy model
	 */
	public SigNotFoundException() {
		super();
	}

}
