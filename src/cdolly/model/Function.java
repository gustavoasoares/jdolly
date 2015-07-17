package cdolly.model;

import java.util.ArrayList;
import java.util.List;

import cdolly.utils.Util;

public class Function {
	
	private VariableType returnType; //TODO refatorar, pois o nome da classe não está condizendo com seu uso aqui
	private String name;
	private List<Variable> params;
	private List<Statement> statements;
	
	public Function(VariableType returnType, String nome){
		this.returnType = returnType;
		this.name = nome;
		this.params = new ArrayList<Variable>();
		this.statements = new ArrayList<Statement>();
	}
	
	public void addParam(Variable param){
		this.params.add(param);
	}
	
	public void addStatement(Statement statement){
		if (statement == null){
			System.out.println("VVVVVVVVVVVVVai acrescentar statement null");
		}
		this.statements.add(statement);
	}

	public VariableType getReturnType() {
		return returnType;
	}

	public void setType(VariableType returnType) {
		this.returnType = returnType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Variable> getParams() {
		return params;
	}

	public void setParams(List<Variable> params) {
		this.params = params;
	}
	
	public String getSource(){
		StringBuffer paramsDeclaration = new StringBuffer("  ");
		for (Variable param: params){
			paramsDeclaration.append(param.getType().toString() + " " + param.getName() + ",");
		}
		String finalParams = Util.removeLastChar(paramsDeclaration.toString());
		
		StringBuffer statementsDeclaration = new StringBuffer();
		int st = 1;
		for (Statement statement: statements){
			statementsDeclaration.append("\n" + statement.getSource());
		}
		
		return returnType.toString() + " " + getName() + " (" + finalParams + "){\n" + statementsDeclaration.toString() + "\n}";
	}
	
}
