package com.my.compiler.drlang;

public class Error {

	public static void drlerror(int exit_code, String message) {

		System.err.println(message);
		System.exit(exit_code);
	}

	public static void drlerror(int exit_code, String ... message) {

		for (String x : message)
			System.err.print(x);
		System.err.println();
		System.exit(exit_code);
	}

	public static void drlerror(String message, Exception ex, int exit_code) {

		System.err.println(message);
		ex.printStackTrace();
		System.exit(exit_code);
	}

	public static void drlerror(int exit_code, Exception ex, String ... message) {

		for (String x : message)
			System.err.print(x);
		System.err.println();
		ex.printStackTrace();
		System.exit(exit_code);
	}
}