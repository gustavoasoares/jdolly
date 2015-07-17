package cdolly.generator;

/**
 * @author Jeanderson Candido<br>
 *         <a href="http://jeandersonbc.github.io"
 *         target="_blank">http://jeandersonbc.github.io</a>
 */
public class UnexistingModelException extends Exception {

	private static final long serialVersionUID = 1L;

	public UnexistingModelException(String alloyModelPath) {
		super("Could not find alloy model for the given path \""
				+ alloyModelPath + "\".");
	}

	public UnexistingModelException() {
		super();
	}

}
