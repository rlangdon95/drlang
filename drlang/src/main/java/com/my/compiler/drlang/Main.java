package com.my.compiler.drlang;

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.util.regex.Pattern;

/**
 * Hello world!
 *
 */
public class Main {

	public static TokenKind getTokenKind(String token, int line_number, int offset) {

		for (TokenPattern x : Constants.KEYWORDS) {

			Pattern pat = Pattern.compile(x.getName());
			boolean match = pat.matcher(token).matches();
			if (match) {

				return x.getKind();
			}
		}

		System.err.println("Unidentifiable Token (" + token + ") at line " + line_number + " and column " + offset + ". Exiting.");
		for (int i = 1; i < offset; ++i)
			System.err.print(" ");
		System.err.println("^");
		System.exit(255);
		return null;
	}

	public static Token makeToken(String str, int id, int line_number, int offset) {

		Token t1 = new Token(str, id, line_number, offset, getTokenKind(str, line_number, offset));
		return t1;
	}

	public static List<Token> lexer(String filename) {

		File file = new File(filename);
		if (!file.exists()) {
	      
	    	System.err.println(filename + " does not exist.");
	    	System.exit(255);
	    }

	    if (!(file.isFile() && file.canRead())) {
			
			System.err.println(file.getName() + " cannot be read from.");
			System.exit(255);
		}

	    // List of tokens
		List<Token> token_list = new ArrayList<Token>();
		
		StringBuilder buffer = new StringBuilder("");

		// current char of the SLoC
		char current = '\0';

		// track the current line number
		int line_number = 1;

		// track the offset at which the character occurs in the given line
		int offset = 0;

		// count the number of tokens to assign unique IDs to tokens
		int token_id = 0;

		// a boolean flag to check if the the current space, tab, linefeed, or carriage return is part of the string or code
		boolean string_sequence = false;

		try (FileInputStream fis = new FileInputStream(file)) {

			if (fis.available() > 0)
				current = (char) fis.read();

			while (fis.available() > 0) {

				if (current == '\n') {

					++line_number;
					offset = 0;
				}

				if ((current == ' ' || current == '\t' || current == '\n' || current == '\r' || current == ';') && !string_sequence) {

					if (buffer.length() > 0) {
						
						token_list.add(makeToken(buffer.toString(), token_id, line_number, offset));
						buffer = new StringBuilder("");
						if (current == ';') {

							buffer.append(current);
							current = (char) fis.read();
							++offset;
							continue;
						}
					}
					
					while ((current == ' ' || current == '\t' || current == '\n' || current == '\r') && (fis.available() > 0)) {

						current = (char) fis.read();
						++offset;
						if (current == '\n') {

							++line_number;
							offset = 0;
						}
					}

					continue;
				}

				buffer.append(current);
				++offset;
				current = (char) fis.read();
			}

			buffer.append(current);
			current = (char) fis.read();
			++offset;
		}

		catch (IOException e) {

			e.printStackTrace();
		}

		if (buffer.length() > 0)
			token_list.add(makeToken(buffer.toString(), token_id, line_number, offset));

		return token_list;

		/* try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			
			String line = br.readLine();
			char ch = 
			int line_number = 1;
			while (line != null) {

				int offset = line.length();
				line = line.trim();
				offset = offset - line.length();
				if (line.length() != 0)
					tokenizer(line, line_number, offset);

				line = br.readLine();
				++line_number;
			}
		}
		
		catch (IOException e) {

			e.printStackTrace();
		}*/
	}

	public static void init() {}

    public static void main(String[] args) {
        
        // if (args.length != 1)
        	// System.out.println("Incorrect Usage!");

    	init();

    	// Pattern p = Pattern.compile("([a-zA-Z][a-zA-Z0-9_$]*\\(\\))");
    	// System.out.println("Output: " + p.matcher("main()").matches());

        // Lexical Analysis
    	List<Token> token_list = lexer("C:/Users/am250135/personal/drlang/drlang/src/test/java/com/my/compiler/drlang/input/test1.drl");
    	for (Token x : token_list)
    		System.out.println(x.getKind() + "\t" + x.getName());

        // Syntax Analysis

        // Semantic Analysis

        // Intermediate Code Generation

        // Optimization : This has very low priority for now

        // Code generation : Currently, convert DRLang code to Assembly
    }
}
