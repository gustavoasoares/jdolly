package jdolly;

public class JDollyFactory {
	
	private static JDollyFactory _instance = null;
	
	private JDollyFactory() {};
	
	public static JDollyFactory getInstance() {
		if (_instance == null)
			_instance = new JDollyFactory();
		return _instance;
	}
	
	
	public JDolly createJDolly(Scope scope, String theory) {
		
		if (scope == null)
			return new JDollyImp(theory);			
		if (scope.getMaxField() > 0)
			return new JDollyImp(theory, scope.getMaxPackage(), scope.getMaxClass(),scope.getMaxMethod(), scope.getMaxField());
		
		return new JDollyImp(theory, scope.getMaxPackage(), scope.getMaxClass(),scope.getMaxMethod());				
	}

}
