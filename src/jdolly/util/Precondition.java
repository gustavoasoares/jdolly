package jdolly.util;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class Precondition {
	
	
	
	private String message;
	
	private List<String> programs;
	
	private int qnt;
	
	private static Dictionary _instances = new Hashtable();

	private Precondition(String message, String program) {
		this.message = message;
		qnt = 1;
		programs = new ArrayList<String>();
		programs.add(program);
	}
	
	static void loadPrecondition(String message, String program) {
		new Precondition(message, program).store();
	}
	
	private void store() {
        _instances.put(this.getMessage(), this);
    }
	
	public static Precondition getNamed(String message) {
        return (Precondition) _instances.get(message);
    }
	
	public static Dictionary getAll() {
		return _instances;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getPrograms() {
		return programs;
	}

	public void setPrograms(List<String> programs) {
		this.programs = programs;
	}

	public int getQnt() {
		return qnt;
	}

	public void setQnt(int qnt) {
		this.qnt = qnt;
		
	}	
	
}
