package jdolly.examples;
import jdolly.util.TimeInterval;


public class Main {
	
	public static void main(String[] args) {
	
	Gen g = new PullUpMethodGen();	
	long start = System.currentTimeMillis();
	g.generatePrograms(true, true, false);
	long stop = System.currentTimeMillis();
	TimeInterval timeInterval = new TimeInterval(start, stop);
	System.out.println("Tempo: " + timeInterval.intervalInSecsToStr());
	}
	
	

}
