package com.my.compiler.drlang;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class CompilerUtils {

	public static void addVariable(Set<Variable> var_set, Token token, Token prev_token, int scope) {
		
		// TODO: Handle cases for all datatypes
		if (prev_token.getKind() == TokenKind.INT) {
			
			Variable var = new Variable(token.getName(), prev_token.getKind(), scope);
			if (var_set.isEmpty() || !var_set.contains(var))
				var_set.add(var);
			
			else {
				
				System.err.println("Variable " + var.getName() + " already defined in scope. Exiting.");
				System.exit(255);
			}
		}
	}
	
	public static TokenKind getTokenKind(String token, int line_number, int offset) {

		for (TokenPattern x : Constants.KEYWORDS) {

			Pattern pat = Pattern.compile(x.getName());
			boolean match = pat.matcher(token).matches();
			if (match)
				return x.getKind();
		}

		System.err.println("Unidentifiable Token <" + token + "> at line " + line_number + " and column " + offset + ". Exiting.");
		System.err.println(token);
		System.exit(255);
		return null;
	}

	public static Token makeToken(String str, int id, int line_number, int offset) {

		Token t1 = new Token(str, id, line_number, offset, getTokenKind(str, line_number, offset));
		return t1;
	}
	
	public static Token makeToken(StringBuilder str, int id, int line_number, int offset) {

		return makeToken(str.toString(), id, line_number, offset);
	}
	
	public static void dispListPretty(List<Token> list) {
		
		int max_size = 0;
		for (Token x : list)
			if (max_size < x.getKind().toString().length())
				max_size = x.getKind().toString().length();
		
		for (Token x : list) {
			
			String str = x.getKind().toString();
			int len = str.length();
			System.out.print(str);
			for (int i = 0; i <= (max_size - len); ++i)
				System.out.print(" ");
			System.out.print(":    ");
			
			System.out.println(x.getName() + ", " + x.getLineNumber() + ", " + x.getOffset());
		}
	}
	
	public static void printAssembly(String filepath) {
		
		try (FileReader fr     = new FileReader(filepath);
			 BufferedReader br = new BufferedReader(fr)) {

			String line = null;
			while ((line = br.readLine()) != null)
				System.out.println(line);
		}
		
		catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	public static int checkSolution(String directory, String output_file) {
		
		String control_file = output_file.substring(0, output_file.lastIndexOf('.')) + ".control";
		try (FileReader fr1 = new FileReader(directory + control_file); BufferedReader br1 = new BufferedReader(fr1);
			 FileReader fr2 = new FileReader(directory + control_file); BufferedReader br2 = new BufferedReader(fr1)) {
			
			String line1 = null;
			String line2 = null;
			int line_number = 0;
			while ((line1 = br1.readLine()) != null) {
				
				line2 = br2.readLine();
				if (line2 == null)
					return line_number;
				
				++line_number;
				if (!line1.equals(line2))
					return line_number;
			}
			
			if (line2 != null)
				return line_number;
		}
		
		catch (IOException e) {

			e.printStackTrace();
		}
		
		return 0;
	}
}
