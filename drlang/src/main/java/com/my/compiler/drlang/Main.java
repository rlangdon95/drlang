package com.my.compiler.drlang;

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * Hello world!
 *
 */
public class Main {

	public static AbsSynTree parser(List<Token> token_list, String filename) {

		// flag to denote if we are currently inside a function
		boolean function_flag = false;

		// flag to denote if we are currently processing a statement
		boolean statement_flag = false;

		// return kind of the next function
		FunctionReturnKind return_kind = FunctionReturnKind.VOID;

		// stack for checking balanced curly braces
		Stack<Boolean> curly_braces_stack = new Stack<Boolean>();

		AbsSynTree tree = new AbsSynTree();
		Node<Program> root = new Node<Program>(new Program(filename), NodeKind.PROGRAM);
		tree.add(root, null);
		Node parent = root;

		for (Token x : token_list) {
			
			switch (x.getKind()) {

				case INT:
				{
					if (!function_flag) {
						
						return_kind = FunctionReturnKind.INT;
						function_flag = true;
					}

					else {

						System.err.print("Line " + x.getLineNumber() + ", column " + x.getOffset() + ": ");
						System.err.println("Multiple return types for function " + x.getName() + ".");
						System.exit(255);
					}
				}
				break;

				case IDENTIFIER_FUNCTION:
				{
					function_flag = true;
					Function fun = new Function(x.getName(), x.getId(), return_kind);
					Node<Function> node = new Node<Function>(fun, NodeKind.FUNCTION);
					tree.add(node, parent);
					parent = node;
					statement_flag = true;
				}
				break;

				case OPEN_CURLY:
				curly_braces_stack.push(true);
				break;

				case CLOSE_CURLY:
				if (curly_braces_stack == null || curly_braces_stack.isEmpty()) {
					
					System.err.println("Unmatched curly braces at line " + x.getLineNumber() + ", column " + x.getOffset() + ".");
					System.exit(255);
				}
				curly_braces_stack.pop();
				break;

				case RETURN:
				{
					Node<Keyword> node = new Node<Keyword>(new Keyword("ret", TokenKind.RETURN), NodeKind.RETURN);
					boolean node_added = tree.add(node, parent);
					if (!node_added) {
						
						System.err.println("Parser error encountered...!!!");
						System.exit(255);
					}
					parent = node;
				}
				break;

				case LITERAL_INT:
				{
					Node<Integer> node = new Node<Integer>(Integer.parseInt(x.getName()), NodeKind.LITERAL);
					tree.add(node, parent);
					parent = node;
				}
				break;

				case SEMICOLON:
				{
					Node<String> node = new Node<String>(";", NodeKind.SEMICOLON);
					tree.add(node, parent);
					parent = node;
					statement_flag = false;
				}
				break;

				default:
				System.err.println("Unidenfied token kind at line " + x.getName() + ", column " + x.getOffset() + ".");
				System.exit(255);
			}
		}

		return tree;
	}

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
						
						token_list.add(makeToken(buffer.toString(), token_id, line_number, offset - 1));
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
			token_list.add(makeToken(buffer.toString(), token_id, line_number, offset - 1));

		return token_list;
	}

	public static void init() {}

    public static void main(String[] args) {
        
        // if (args.length != 1)
        	// System.out.println("Incorrect Usage!");

    	init();

    	// Pattern p = Pattern.compile("([a-zA-Z][a-zA-Z0-9_$]*\\(\\))");
    	// System.out.println("Output: " + p.matcher("main()").matches());

        // Lexical Analysis
    	System.out.println("Starting Lexical Analysis...");
        // String filename = "C:/Users/am250135/personal/drlang/drlang/src/test/java/com/my/compiler/drlang/input/test1.drl";
        String filename = "C:\\Users\\AMARTYA MAJUMDAR\\Documents\\Github\\drlang\\drlang\\src\\test\\java\\com\\my\\compiler\\drlang\\input\\test1.drl";
    	List<Token> token_list = lexer(filename);
    	for (Token x : token_list)
    		System.out.println(x.getKind() + "\t" + x.getName() + ", " + x.getLineNumber() + ", " + x.getOffset());
    	System.out.println("Lexical analysis complete...!!!");
    	
        // Parsing
    	System.out.println("\nStarting Parser...");
        AbsSynTree ast = parser(token_list, filename);
        ast.preorder();
        System.out.println("Parsing complete...!!!");

        // Intermediate Code Generation

        // Optimization : This has very low priority for now

        // Code generation : Currently, convert DRLang code to Assembly
    }
}
