package com.my.compiler.drlang;

import java.io.IOException;
import java.io.OutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * Project:     DRLang, an attempt at a new compiler
 * Description: The compiler is written in Java, which aims at
 *              reading code in DRL and generates equivalent
 *              Assembly code.
 * Author:      Amartya Majumdar
 * Contact:     a.majumdar1041@gmail.com
 * Resources:   Writing a C compiler by Nora Sandler
 *
 */
public class Main {

	public static void evaluateAndReplaceNode(Node<?> node, Node<?> root) {

		switch (node.getKind()) {

			case EXPRESSION:
			{

			}
			break;

			case FUNCTION:
			{

			}
			break;

			default:
			{
				System.err.println("Unexpected Node Kind. Exiting.");
				System.exit(255);
			}
		}
	}

	public static void generate(Node<?> node, Node<?> root, List<String> lines) {

		switch (node.getKind()) {

			case PROGRAM:
			{
				System.err.println("Unexpected Parse Tree. Exiting.");
				System.exit(255);
			}
			break;

			case FUNCTION:
			{
				String function_name = ((Function) node.getData()).getName();
				StringBuilder line = new StringBuilder("\t.globl _");
				line.append(function_name);
				lines.add(line.toString());

				line = new StringBuilder("_");
				line.append(function_name);
				lines.add(line.toString());
				for (Node<?> x : (List<Node<?>>)node.getChildren())
					generate(x, root, lines);
			}
			break;

			case RETURN:
			{
				// Error if return has more than one child
				if (node.getChildren().size() > 1) {

					System.err.println("Unexpected error. Multiple returns. Exiting.");
					System.exit(255);
				}

				String data = node.getData().toString();
				StringBuilder line = new StringBuilder("\t");
				line.append(Constants.MOVEL);
				line.append("\t");
				line.append("$");

				// if the child is of kind LITERAL, no need for evaluation
				// TODO: if the child is of kind EXPRESSION, evaluate the expression
				//       and replace the child with a LITERAL kind node having the evaluated data
				if (node.getChildren().get(0).getKind() == NodeKind.EXPRESSION)
					evaluateAndReplaceNode(node, root);

				// TODO: if the return statement returns the value returned by another function
				else if (node.getChildren().get(0).getKind() == NodeKind.FUNCTION)
					evaluateAndReplaceNode(node, root);

				else {

					System.err.println("Unexpected Node Kind. Exiting.");
					System.exit(255);
				}

				// append the evaluated value to assembly code
				line.append(node.getChildren().get(0).getData().toString());
				line.append(", ");
				line.append(Constants.EAX);
				lines.add(line.toString());

				line = new StringBuilder("\t");
				line.append(Constants.RET);
				lines.add(line.toString());
			}
			break;

			case SEMICOLON:
			{
				lines.add(Constants.LINEFEED);
			}
			break;
		}
	}

	public static void generate(AbsSynTree ast, String filename) {

		File file = new File(filename);
		List<String> lines = new ArrayList<String>();
		
		try (OutputStream os = new FileOutputStream(file, true)) {
	
			Node<?> root = ast.getRoot();
			for (Node<?> x : root.getChildren()) {

				if (((Function) x.getData()).getName().equals("main")) {

					generate(x, root, lines);
					break;
				}
			}

			for (String x : lines)
				os.write(x.getBytes(), 0, x.length());
		}

		catch (IOException ioe) {

			ioe.printStackTrace();
		}
	}

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
		Node<?> parent = root;

		for (Token x : token_list) {

			System.out.println("HELLO: " + parent.getData() + " " + parent.getKind());
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
					tree.add(node, parent);
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
        String filename = "C:/Users/am250135/personal/drlang/drlang/src/test/java/com/my/compiler/drlang/input/test1.drl";
    	List<Token> token_list = lexer(filename);
    	for (Token x : token_list)
    		System.out.println(x.getKind() + "\t" + x.getName() + ", " + x.getLineNumber() + ", " + x.getOffset());

        // Parsing
        AbsSynTree ast = parser(token_list, filename);
        ast.preorder();

        // Intermediate Code Generation

        // Optimization : This has very low priority for now

        // Code generation : Currently, convert DRLang code to Assembly
        String output_filepath = "C:/Users/am250135/personal/drlang/drlang/src/test/java/com/my/compiler/drlang/output/test1.asm";
        generate(ast, output_filepath);
    }
}
