package jdolly;

import java.util.ArrayList;
import java.util.List;

import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.ConstList;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.ErrorSyntax;
import edu.mit.csail.sdg.alloy4compiler.ast.Command;
import edu.mit.csail.sdg.alloy4compiler.ast.CommandScope;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Options;
import edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod;
import jdolly.util.Util;

public class JDollyImp extends JDolly {

	public JDollyImp(final String alloyTheory, final int maxPackages, final int maxClasses,
			final int maxMethods, final int maxFields) {
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

	public JDollyImp(final String alloyTheory, final Integer maxPackages,
			final int maxClasses, final int maxMethods) {
		super();
		this.alloyTheory = alloyTheory;
		this.maxPackages = maxPackages;
		this.maxClasses = maxClasses;
		this.maxClassNames = maxClasses;
		this.maxMethods = maxMethods;
		this.maxMethodNames = maxMethods;
		this.maxMethodBody = maxMethods;
	}

	public JDollyImp(final String alloyTheory) {
		super();
		this.alloyTheory = alloyTheory;

	}

	private ConstList<CommandScope> createScopeList() throws ErrorSyntax {

		List<CommandScope> result = new ArrayList<CommandScope>();

		final Sig type = createSigByName("Class");
		final Sig method = createSigByName("Method");
		final Sig methodId = createSigByName("MethodId");
		final Sig classId = createSigByName("ClassId");
		final Sig package_ = createSigByName("Package");
		final Sig body = createSigByName("Body");
		final Sig field = createSigByName("Field");
		final Sig fieldId = createSigByName("FieldId");

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

				A4Options options = Util.defHowExecCommands();
				
				Util.printCommand(command);

				currentAns = TranslateAlloyToKodkod.execute_command(rep,
						javaMetamodel.getAllReachableSigs(), command, options);
			}
		} catch (Err e) {
			e.printStackTrace();
		}
	}

}
