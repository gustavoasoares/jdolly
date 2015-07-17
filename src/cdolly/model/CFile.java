package cdolly.model;

import java.util.ArrayList;
import java.util.List;

public class CFile {
	
	private List<Function> functions;
	private List<VarDeclarationStatement> globalVariables;
	private List<Define> defines;
	
	public CFile() {
		this.functions = new ArrayList<Function>();
		this.globalVariables = new ArrayList<VarDeclarationStatement>();
		this.defines = new ArrayList<Define>();
	}

	public void addFunction(Function function){
		this.functions.add(function);
	}
	
	public void addGlobalVariable(VarDeclarationStatement variable){
		this.globalVariables.add(variable);
	}

	public void addDefine(Define define){
		this.defines.add(define);
	}
	
	public String getCSource(){
		StringBuffer source = new StringBuffer();
		for (Define define : defines) {
			source.append("\n" + define.getSource());
		}
		for (VarDeclarationStatement globalVariable: globalVariables){
			source.append("\n" + globalVariable.getSource());
		}
		for (Function function: functions){
			source.append("\n" + function.getSource());
		}
		return source.toString();
	}


}
