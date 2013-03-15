package jdolly.util;

import java.io.File;
import java.io.FileFilter;

public class NetbeansCounter {

	public static void main(String[] args) {
		
		String path = "/Users/gustavo/Doutorado/experiments/refactoring-constraints-new/renamefield/last";
		File refactoring = new File(path);
		File[] tests = refactoring.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (pathname.getName().startsWith("test"))
					return true;
				else
					return false;
			}

		});

		int count = 0;
		int i = 0;
		
		for (File test : tests) {
			
			File outNetBeans = new File(test,"out/netbeans");
			
			if ( !outNetBeans.exists())
				count++;
			i++;
			
		}
		System.out.println("total de programas: " + i);
		System.out.println("programs não refatorados pelo netbeans: " + count);
	}
}
