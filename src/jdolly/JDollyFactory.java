package jdolly;

public final class JDollyFactory {
	
	private static JDollyFactory jDollyInstance = null;
	
	private JDollyFactory() {};
	
	public static JDollyFactory getInstance() {
		if (jDollyInstance == null) {
			jDollyInstance = new JDollyFactory();
		}
		return jDollyInstance;
	}
	
	public static JDolly createJDolly(final Scope scope, final String theory) {
		return getInstance().createJDollyAux(scope, theory);				
	}

	private JDolly createJDollyAux(final Scope scope, final String theory) {
		if (scope == null) {
			return new JDollyImp(theory);
		}
		if (scope.getMaxField() > 0) {
			return new JDollyImp(theory, scope.getMaxPackage(),
					scope.getMaxClass(),
					scope.getMaxMethod(),
					scope.getMaxField());
		}
		return new JDollyImp(theory, scope.getMaxPackage(),
				scope.getMaxClass(), scope.getMaxMethod());
	}

}
