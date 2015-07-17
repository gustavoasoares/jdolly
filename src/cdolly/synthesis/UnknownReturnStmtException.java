package cdolly.synthesis;

public class UnknownReturnStmtException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnknownReturnStmtException(String unknownRawType) {
		super("Could not match raw return exception \"" + unknownRawType
				+ "\" with a Return Statement");
	}
}
