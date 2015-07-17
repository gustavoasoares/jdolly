package cdolly.model;

import java.util.Random;

public class RandomValueGenerator {
	
	private static String[] intValues = new String[]{"-1", "0", "1", "2", "4", "10", "100"};
	private static String[] floatValues = new String[]{"1.1", "0", "2.0", "4.5", "-1.4", "-10.1", "100"};
	private static String[] charValues = new String[]{"'a'", "'b'", "'0'", "'2'", "'4'", "'Z'", "'A'"};
	
	public static String getInt(){
		return intValues[getIndex(intValues.length)];
	}
	
	public static String getFloat(){
		return intValues[getIndex(floatValues.length)];
	}
	
	public static String getChar(){
		return intValues[getIndex(charValues.length)];
	}
	
	private static int getIndex(int maxArrayLength){
		Random rand = new Random(); 
		return rand.nextInt(maxArrayLength); 
	}

}
