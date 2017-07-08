package jdolly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.testorrery.ForLoopIterator;
import org.testorrery.ForLoopWithJumpIterator;
import org.testorrery.Generator;
import org.testorrery.IGenerator;

import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.ConstList;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.ErrorWarning;
import edu.mit.csail.sdg.alloy4compiler.ast.Module;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;

public abstract class JDolly extends Generator<List<CompilationUnit>> {

	AlloyToJavaTranslator extractor;

	private int currentProgram = 0;

	protected A4Solution currentAns;

	private boolean isFirstTime = true;

	protected String alloyTheory;

	protected int maxPackages;

	protected int maxClasses;

	protected int maxClassNames;

	protected int maxMethods;

	protected int maxMethodNames;

	protected boolean isExactMaxClasses = false;

	protected Module javaMetamodel;

	protected boolean isExactMaxClassNames = false;

	protected boolean isExactMaxPackages = false;

	protected boolean isExactMaxMethods = false;

	protected boolean isExactMaxMethodNames = false;

	protected boolean isExactMethodBodyScope = false;

	protected int maxMethodBody;

	protected Integer maxFields;

	protected boolean isExactMaxFields = false;

	protected boolean isExactMaxFieldnames = false;

	protected int maxFieldNames;
	
	public JDolly() {
		super();
	}

	protected abstract void initializeAlloyAnalyzer();

	@Override
	protected List<CompilationUnit> generateNext() {
		currentProgram++;
		
		List<CompilationUnit> result = new ArrayList<CompilationUnit>();

		System.out
				.println("Program "
						+ currentProgram);
		
		extractor = new AlloyToJavaTranslator(currentAns);
		result = extractor.getJavaCode();

		return result;
	}

	@Override
	public boolean hasNext() {
		// primeira vez
		if (isFirstTime) {
			initializeAlloyAnalyzer();
		}
		final boolean isCurrAnsSatisfiable = currentAns.satisfiable();
		if (isCurrAnsSatisfiable && isFirstTime) {			
			isFirstTime = false;
			return true;
		}
		final boolean isLastProgram = maximumPrograms > 0 && maximumPrograms == currentProgram;
		if (isLastProgram) {
			return false;
		}

		boolean result = true;
		try {
			final boolean isNextAnsSatisfiable = currentAns.next().satisfiable();
			final boolean isCurrAnsEqualToNextAns = currentAns.equals(currentAns.next());
			if (!isNextAnsSatisfiable || isCurrAnsEqualToNextAns)
				result = false;
			
			currentAns = currentAns.next();
			
		} catch (Err e) {
			result = false;
			e.printStackTrace();
		}

		return result;
	}
	
	public int getMaxClassNames() {
		return maxClassNames;
	}

	public void setMaxClassNames(int maxClassNames) {
		this.maxClassNames = maxClassNames;
	}

	public boolean isExactMaxClasses() {
		return isExactMaxClasses;
	}

	public void setExactMaxClasses(boolean isExactMaxClasses) {
		this.isExactMaxClasses = isExactMaxClasses;
	}

	public int getMaxMethodNames() {
		return maxMethodNames;
	}

	public void setMaxMethodNames(int maxMethodNames) {
		this.maxMethodNames = maxMethodNames;
	}

	public boolean isExactMaxClassNames() {
		return isExactMaxClassNames;
	}

	public void setExactMaxClassNames(boolean isExactMaxClassNames) {
		this.isExactMaxClassNames = isExactMaxClassNames;
	}

	public boolean isExactMaxPackages() {
		return isExactMaxPackages;
	}

	public void setExactMaxPackages(boolean isExactMaxPackages) {
		this.isExactMaxPackages = isExactMaxPackages;
	}

	public boolean isExactMaxMethods() {
		return isExactMaxMethods;
	}

	public void setExactMaxMethods(boolean isExactMaxMethods) {
		this.isExactMaxMethods = isExactMaxMethods;
	}

	public boolean isExactMaxMethodNames() {
		return isExactMaxMethodNames;
	}

	public void setExactMaxMethodNames(boolean isExactMaxMethodNames) {
		this.isExactMaxMethodNames = isExactMaxMethodNames;
	}

	public boolean isExactMethodBodyScope() {
		return isExactMethodBodyScope;
	}

	public void setExactMethodBodyScope(boolean isExactMethodBodyScope) {
		this.isExactMethodBodyScope = isExactMethodBodyScope;
	}

	public int getMaxMethodBody() {
		return maxMethodBody;
	}

	public void setMaxMethodBody(int maxMethodBody) {
		this.maxMethodBody = maxMethodBody;
	}

	public boolean isExactMaxFields() {
		return isExactMaxFields;
	}

	public void setExactMaxFields(boolean isExactMaxFields) {
		this.isExactMaxFields = isExactMaxFields;
	}

	public boolean isExactMaxFieldnames() {
		return isExactMaxFieldnames;
	}

	public void setExactMaxFieldnames(boolean isExactMaxFieldnames) {
		this.isExactMaxFieldnames = isExactMaxFieldnames;
	}

	public int getMaxFieldNames() {
		return maxFieldNames;
	}

	public void setMaxFieldNames(int maxFieldNames) {
		this.maxFieldNames = maxFieldNames;
	}

	protected static A4Reporter createA4Reporter() {
		return new A4Reporter() {
			@Override
			public void warning(ErrorWarning msg) {
				System.out.print("Relevance Warning:\n"
						+ (msg.toString().trim()) + "\n\n");
				System.out.flush();
			}
		};
	}
	
	protected Sig createSignatureBy(String entityName) {
		Sig result = null;
		ConstList<Sig> sigsDefined = javaMetamodel.getAllReachableSigs();
		for (Sig sig : sigsDefined) {
			String label = sig.label.replaceAll("[^/]*/", "");
			if (label.equals(entityName))
				result = sig;
		}
		return result;
	}

	public A4Solution getInstance(int i) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	

}
