package jdolly.examples;



public class Main {
	

	
	
	public static void main(String[] args) {
	
	Gen g = new PullUpMethodGen();	
	long start = System.currentTimeMillis();
	g.generatePrograms(true, true, false);
	long stop = System.currentTimeMillis();
	long total = stop - start;
	System.out.println("Tempo: " + total);
	}
	
	

}
