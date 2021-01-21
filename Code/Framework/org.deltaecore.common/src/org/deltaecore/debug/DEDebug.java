package org.deltaecore.debug;

public class DEDebug {
	public static boolean debugEnabled = true;
	
	public static void println() {
		println("");
	}
	
	public static void println(String message) {
		if (debugEnabled) {
			System.out.println("DEBUG: " + message);
		}
	}
}
