package jdolly;

import java.util.ArrayList;
import java.util.List;

import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.ConstList;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.ErrorSyntax;
import edu.mit.csail.sdg.alloy4.ErrorWarning;
import edu.mit.csail.sdg.alloy4compiler.ast.Command;
import edu.mit.csail.sdg.alloy4compiler.ast.CommandScope;
import edu.mit.csail.sdg.alloy4compiler.ast.Module;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Options;
import edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod;

public class JDollyImp extends JDolly {

	public JDollyImp(String alloyTheory, int maxPackages, int maxClasses,
			int maxMethods, int maxFields) {
		super();
		this.alloyTheory = alloyTheory;
		this.maxPackages = maxPackages;
		this.maxClasses = maxClasses;
		this.maxClassNames = maxClasses;
		this.maxMethods = maxMethods;
		this.maxMethodNames = maxMethods;
		this.maxMethodBody = maxMethods;
		this.maxFields = maxFields;
		this.maxFieldNames = maxFields;

	}

	public JDollyImp(String alloyTheory, Integer maxPackages,
			int maxClasses, int maxMethods) {
		super();
		this.alloyTheory = alloyTheory;
		this.maxPackages = maxPackages;
		this.maxClasses = maxClasses;
		this.maxClassNames = maxClasses;
		this.maxMethods = maxMethods;
		this.maxMethodNames = maxMethods;
		this.maxMethodBody = maxMethods;
	}

	public JDollyImp(String alloyTheory) {
		super();
		this.alloyTheory = alloyTheory;

	}

	private ConstList<CommandScope> createScopeList() throws ErrorSyntax {

		List<CommandScope> result = new ArrayList<CommandScope>();

		Sig type = createSigByName("Class");
		Sig method = createSigByName("Method");
		Sig methodId = createSigByName("MethodId");
		Sig classId = createSigByName("ClassId");
		Sig package_ = createSigByName("Package");
		Sig body = createSigByName("Body");
		Sig field = createSigByName("Field");
		Sig fieldId = createSigByName("FieldId");

		CommandScope packageScope = new CommandScope(package_,
				isExactMaxPackages, maxPackages);
		result.add(packageScope);

		CommandScope typeScope = new CommandScope(type, isExactMaxClasses(),
				maxClasses);
		result.add(typeScope);

		CommandScope classIdScope = new CommandScope(classId,
				isExactMaxClassNames, maxClassNames);
		result.add(classIdScope);

		CommandScope methodScope = new CommandScope(method, isExactMaxMethods,
				maxMethods);
		result.add(methodScope);

		CommandScope methodIdScope = new CommandScope(methodId,
				isExactMaxMethodNames, maxMethodNames);
		result.add(methodIdScope);

		CommandScope bodyScope = new CommandScope(body, isExactMethodBodyScope,
				maxMethodBody);
		result.add(bodyScope);

		if (this.maxFields != null) {
			CommandScope fieldScope = new CommandScope(field, isExactMaxFields,
					maxFields);
			result.add(fieldScope);

			CommandScope fieldIdScope = new CommandScope(fieldId,
					isExactMaxFieldnames, maxFieldNames);
			result.add(fieldIdScope);

			
		}
		return ConstList.make(result);
	}

	@Override
	protected void initializeAlloyAnalyzer() {
		// Alloy4 sends diagnostic messages and progress reports to the
		// A4Reporter.
		A4Reporter rep = createA4Reporter();

		try {

			javaMetamodel = CompUtil.parseEverything_fromFile(rep, null,
					alloyTheory);

			for (Command command : javaMetamodel.getAllCommands()) {

				ConstList<CommandScope> constList = createScopeList();

				command = command.change(constList);

				// Choose some default options for how you want to execute the
				// commands
				A4Options options = new A4Options();
				options.solver = A4Options.SatSolver.SAT4J;

				// Execute the command
				System.out.println("============ Command " + command
						+ ": ============");

				currentAns = TranslateAlloyToKodkod.execute_command(rep,
						javaMetamodel.getAllReachableSigs(), command, options);
			}
		} catch (Err e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
