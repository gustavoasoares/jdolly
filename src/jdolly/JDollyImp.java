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

		final Sig type = createSignatureBy("Class");
		final Sig method = createSignatureBy("Method");
		final Sig methodId = createSignatureBy("MethodId");
		final Sig classId = createSignatureBy("ClassId");
		final Sig package_ = createSignatureBy("Package");
		final Sig body = createSignatureBy("Body");
		final Sig field = createSignatureBy("Field");
		final Sig fieldId = createSignatureBy("FieldId");

		final CommandScope packageScope = new CommandScope(package_,
				isExactMaxPackages, maxPackages);
		result.add(packageScope);

		final CommandScope typeScope = new CommandScope(type, isExactMaxClasses(),
				maxClasses);
		result.add(typeScope);

		final CommandScope classIdScope = new CommandScope(classId,
				isExactMaxClassNames, maxClassNames);
		result.add(classIdScope);

		final CommandScope methodScope = new CommandScope(method, isExactMaxMethods,
				maxMethods);
		result.add(methodScope);

		final CommandScope methodIdScope = new CommandScope(methodId,
				isExactMaxMethodNames, maxMethodNames);
		result.add(methodIdScope);

		final CommandScope bodyScope = new CommandScope(body, 
				isExactMethodBodyScope, maxMethodBody);
		result.add(bodyScope);

		if (this.maxFields != null) {
			final CommandScope fieldScope = new CommandScope(field, isExactMaxFields,
					maxFields);
			result.add(fieldScope);

			final CommandScope fieldIdScope = new CommandScope(fieldId,
					isExactMaxFieldnames, maxFieldNames);
			result.add(fieldIdScope);

			
		}
		return ConstList.make(result);
	}

	@Override
	protected void initializeAlloyAnalyzer() {
		// Alloy4 sends diagnostic messages and progress reports to the
		// A4Reporter.
		A4Reporter compilationIssuesReport = createA4Reporter();

		try {
			defineCurrentAns(compilationIssuesReport);
		} catch (Err e) {
			e.printStackTrace();
		}
	}

	private void defineCurrentAns(A4Reporter compilationIssuesReport) throws Err, ErrorSyntax {
		
		javaMetamodel = CompUtil.parseEverything_fromFile(compilationIssuesReport, null,
				alloyTheory);

		final ConstList<Command> commandsInModule = javaMetamodel.getAllCommands();
		
		for (Command currentCommand : commandsInModule) {
			Command currentCmdWithOtherScope = modifyCurrentCmdScope(currentCommand);
			Util.printCommand(currentCmdWithOtherScope);
			
			A4Options options = Util.defHowExecCommands();
			currentAns = TranslateAlloyToKodkod.execute_command(compilationIssuesReport,
					javaMetamodel.getAllReachableSigs(), currentCmdWithOtherScope, options);
		}
	}

	private Command modifyCurrentCmdScope(Command currentCommand) throws ErrorSyntax {
		ConstList<CommandScope> constList = createScopeList();

		Command currentCmdWithOtherScope = currentCommand.change(constList);
		
		return currentCmdWithOtherScope;
	}

}
