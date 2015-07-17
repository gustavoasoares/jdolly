package cdolly.synthesis;

public class UnknownRawTypeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnknownRawTypeException(String unknownRawType) {
		super("Could not match raw type \"" + unknownRawType + "\" with a Type");
	}

}
