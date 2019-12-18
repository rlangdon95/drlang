package com.my.compiler.drlang;

import java.io.IOException;
import java.io.OutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
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
	
	public static int counter = 0;
	
	public static final String INPUT_DIRECTORY = "src\\test\\java\\com\\my\\compiler\\drlang\\input\\stage1\\";
	
	public static final String OUTPUT_DIRECTORY = "src\\test\\java\\com\\my\\compiler\\drlang\\output\\stage1\\";
	
	// TODO: change return kind from int to something more generic - String maybe
	//       or Value object :: {Object value; ValueKind kind}
	public static int evaluateExpressionTree(Node<?> node, Node<?> parent, Node<?> root) {
		
		if (node == null) {
			
			System.err.println("Bad execution. Exiting.");
			System.exit(255);
		}
		
		if (node.isLeaf() && (node.getKind() == NodeKind.LITERAL)) {
			
			int value = Integer.parseInt((String)node.getData());
			node = new Node<String>((String)node.getData(), NodeKind.LITERAL);
			return value;
		}

		int value = 0;
		
		switch (node.getKind()) {
		
			case LITERAL:
			{
				return (int)node.getData();
			}
		
			case NOT_OP:
			{
				
			}
			break;
			
			case POSITIVE_OP:
			{
				value = evaluateExpressionTree(node.getChildren().get(0), node, root);
				node = new Node<String>(Integer.toString(value), NodeKind.LITERAL);
				node.setParent(parent);
			}
			break;
			
			case NEGATE_OP:
			{
				int lvalue = evaluateExpressionTree(node.getChildren().get(0), node, root);
				value = -lvalue;
				node = new Node<String>(Integer.toString(value), NodeKind.LITERAL);
				node.setParent(parent);
			}
			break;
			
			case COMPLEMENT_OP:
			{
				int lvalue = evaluateExpressionTree(node.getChildren().get(0), node, root);
				value = -lvalue + 1;
				node = new Node<String>(Integer.toString(value), NodeKind.LITERAL);
				node.setParent(parent);
			}
			break;
		
			case PLUS_OP:
			{
				int lvalue = evaluateExpressionTree(node.getChildren().get(0), node, root);
				int rvalue = evaluateExpressionTree(node.getChildren().get(1), node, root);
				value = lvalue + rvalue;
				node = new Node<String>(Integer.toString(value), NodeKind.LITERAL);
				node.setParent(parent);
			}
			break;
			
			case MINUS_OP:
			{
				int lvalue = evaluateExpressionTree(node.getChildren().get(0), node, root);
				int rvalue = evaluateExpressionTree(node.getChildren().get(1), node, root);
				value = lvalue - rvalue;
				node = new Node<String>(Integer.toString(value), NodeKind.LITERAL);
				node.setParent(parent);
			}
			break;
			
			case MULTIPLY_OP:
			{
				int lvalue = evaluateExpressionTree(node.getChildren().get(0), node, root);
				int rvalue = evaluateExpressionTree(node.getChildren().get(1), node, root);
				value = lvalue * rvalue;
				node = new Node<String>(Integer.toString(value), NodeKind.LITERAL);
				node.setParent(parent);
			}
			break;
			
			case DIVISION_OP:
			{
				int lvalue = evaluateExpressionTree(node.getChildren().get(0), node, root);
				int rvalue = evaluateExpressionTree(node.getChildren().get(1), node, root);
				value = lvalue / rvalue;
				node = new Node<String>(Integer.toString(value), NodeKind.LITERAL);
				node.setParent(parent);
			}
			break;
			
			case EXPONENT_OP:
			{
				int lvalue = evaluateExpressionTree(node.getChildren().get(0), node, root);
				int rvalue = evaluateExpressionTree(node.getChildren().get(1), node, root);
				long temp = (long) Math.round(Math.pow(lvalue, rvalue));
				node = new Node<String>(Long.toString(temp), NodeKind.LITERAL);
				node.setParent(parent);
				value = (int)temp;
			}
			break;
			
			case OR_OP:
			case AND_OP:
			case XOR_OP:
			case EQUALS_OP:
			case NOTEQUALS_OP:
			{
				
			}
			break;
			
			case EXPRESSION:
			{
				if (node.isLeaf()) {
					
					int _value = evaluateExpressionTree(node, parent, root);
					Node<String> temp = new Node<String>(Integer.toString(_value), NodeKind.LITERAL);
					node.addChild(temp);
				}
				else {
					
					int _value = evaluateExpressionTree(node.getChildren().get(0), node, root);
					Node<String> temp = new Node<String>(Integer.toString(_value), NodeKind.LITERAL);
					node.getChildren().set(0, temp);
				}
			}
			break;
			case ASSIGN_OP:
			{
				// TODO:
			}
			break;
			
			case FUNCTION:
			{
				// TODO:
			}
			break;
			
			case NOKIND:
			case LEFT_PARENTHESIS:
			case PROGRAM:
			case RETURN:
			case RIGHT_PARENTHESIS:
			case SEMICOLON:
			default:
			{
				System.err.println("Unexpected node kind in expression. Exiting.");
				System.exit(255);
			}
			break;
		}
		
		return value;
	}

	public static void evaluateAndReplaceNode(Node<?> parent, Node<?> node, int child_index, Node<?> root) {

		switch (node.getKind()) {

			case EXPRESSION:
			{
				System.out.println("DEBUG: " + node.getData() + "  " + node.getKind() + "  " + node.isLeaf());
				parent.getChildren().set(child_index, node);
				if (node.isLeaf()) {
					
					int value = evaluateExpressionTree(node, parent, root);
					Node<String> temp = new Node<String>(Integer.toString(value), NodeKind.LITERAL);
					node.addChild(temp);
				}
				else {
					
					int value = evaluateExpressionTree(node.getChildren().get(0), node, root);
					Node<String> temp = new Node<String>(Integer.toString(value), NodeKind.LITERAL);
					node.getChildren().set(0, temp);
				}
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
				// Error if return type is not void
				if (node.getChildren().size() == 0) {
					
					// Add this condition when function return kind VOID is added 
					// if (getKind() != FunctionReturnKind.VOID)
					System.err.println("Function return type is not VOID.");
					System.exit(255);
				}
				// Error if return has more than one child
				if (node.getChildren().size() > 1) {

					System.err.println("Unexpected error. Multiple returns. Exiting.");
					System.exit(255);
				}

				StringBuilder line = new StringBuilder("\t");
				line.append(Constants.MOVEL);
				line.append("\t");
				line.append("$");

				// if the child is of kind LITERAL, no need for evaluation
				// TODO: if the child is of kind EXPRESSION, evaluate the expression
				//       and replace the child with a LITERAL kind node having the evaluated data
				if (node.getChildren().get(0).getKind() == NodeKind.EXPRESSION)
					evaluateAndReplaceNode(node, node.getChildren().get(0), 0, root);

				// TODO: if the return statement returns the value returned by another function
				else if (node.getChildren().get(0).getKind() == NodeKind.FUNCTION)
					evaluateAndReplaceNode(node, node.getChildren().get(0), 0, root);

				/*else {

					System.err.println("Unexpected Node Kind. Exiting.");
					System.exit(255);
				}*/

				// TODO: this if-else needs to be fixed and made more generic
				// append the evaluated value to assembly code
				if (node.getChildren().get(0).getKind() == NodeKind.EXPRESSION)
					line.append(node.getChildren().get(0).getChildren().get(0).getData().toString());
				else
					line.append(node.getChildren().get(0).getData().toString());
				line.append(", ");
				line.append("%");
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
		default:
			System.err.println("Bad Node Kind. Exiting.");
			System.exit(255);
			break;
		}
	}

	public static void generate(AbsSynTree ast, String filename) {

		File file = new File(filename);
		if (file.exists())
			file.delete();

		List<String> lines = new ArrayList<String>();
		
		try (OutputStream os = new FileOutputStream(file, true)) {
	
			Node<?> root = ast.getRoot();
			for (Node<?> x : root.getChildren()) {

				if (((Function) x.getData()).getName().equals("main()")) {

					generate(x, root, lines);
					break;
				}
			}

			for (String x : lines) {

				os.write(x.getBytes(), 0, x.length());
				os.write(Constants.LINEFEED.getBytes(), 0, Constants.LINEFEED.length());
			}
		}

		catch (IOException ioe) {

			System.err.println("Error in generating assembly file. Exiting.");
			ioe.printStackTrace();
			System.exit(255);
		}
	}

	public static AbsSynTree parser(List<Token> token_list, String filename) {

		// index location of the current token being processed from the token list
		int index = -1;

		// flag to denote if we are currently inside a function
		boolean function_flag = false;

		// flag to denote if we are currently processing a statement
		boolean statement_flag = false;

		// flag to denote if we are in the middle of reading an expression
		boolean expression_flag = false;

		// form the expression
		StringBuilder expression = new StringBuilder("");

		// return kind of the next function
		FunctionReturnKind return_kind = FunctionReturnKind.VOID;

		// stack for checking balanced curly braces
		Stack<Boolean> curly_braces_stack = new Stack<Boolean>();

		AbsSynTree tree = new AbsSynTree();
		Node<Program> root = new Node<Program>(new Program(filename), NodeKind.PROGRAM);
		tree.add(root, null);
		Node<?> parent = root;

		for (Token x : token_list) {

			++index;
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
				}
				break;

				case OPEN_PARENTHESIS:
				{
					expression_flag = true;
				}
				break;

				case CLOSE_PARENTHESIS:
				{
					if (expression_flag)
						expression.append(x.getName());
				}
				break;

				case OPEN_CURLY:
				{
					curly_braces_stack.push(true);
					if (expression_flag) {

						Node<?> node = new Node<String>(expression.toString(), NodeKind.EXPRESSION);
						tree.add(node, parent);
						parent = node;
						expression_flag = false;
						expression = new StringBuilder("");
					}
				}
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
					statement_flag = true;
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
					expression_flag = true;
					expression.append(x.getName());
				}
				break;

				case PLUS_OP:
				case MINUS_OP:
				case MULTIPLY_OP:
				case DIVISION_OP:
				{
					if (expression_flag) {

						expression.append(x.getName());
						System.out.println("BINARY OP: " + expression.toString());
					}
				}
				break;

				case TERM:
				{
					expression_flag = true;
					expression.append(x.getName());
				}
				break;

				case EXPRESSION:
				{
					if (!expression_flag)
						expression_flag = true;
					expression.append(x.getName());
				}
				break;

				case SEMICOLON:
				{
					if (expression_flag) {

						Node<?> node = new Node<String>(expression.toString(), NodeKind.EXPRESSION);
						tree.add(node, parent);
						parent = node;
						expression_flag = false;
						expression = new StringBuilder("");
					}
					statement_flag = false;
				}
				break;

				default:
				System.err.println("Unidentified token kind " + x.getKind() + " at line " + x.getLineNumber() + ", column " + x.getOffset() + ".");
				System.exit(255);
			}
		}

		return tree;
	}

	public static TokenKind getTokenKind(String token, int line_number, int offset) {

		int index = 0;
		for (TokenPattern x : Constants.KEYWORDS) {

			Pattern pat = Pattern.compile(x.getName());
			boolean match = pat.matcher(token).matches();
			++index;
			if (match)
				return x.getKind();
		}

		System.err.println("Unidentifiable Token <" + token + "> at line " + line_number + " and column " + offset + ". Exiting.");
		System.err.println(token);
		/*for (int i = 1; i < offset; ++i)
			System.err.print(" ");
		System.err.println("^"); */
		System.exit(255);
		return null;
	}

	public static Token makeToken(String str, int id, int line_number, int offset) {

		Token t1 = new Token(str, id, line_number, offset, getTokenKind(str, line_number, offset));
		// System.out.println("HELLO: " + t1.getKind());
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

		// a boolean flag to denote if we are in the middle of reading a token
		boolean reading_token = false;

		// backup variable to store previously read token
		Token prev_token = null;

		try (FileInputStream fis = new FileInputStream(file)) {

			if (fis.available() > 0)
				current = (char) fis.read();

			while (fis.available() > 0) {

				// System.out.println("DEBUG: " + current + " " + reading_token);

				if (current == '\n') {

					++line_number;
					offset = 0;
				}

				if (!string_sequence && (current == '(' || current == ')')) {
					
					if (!reading_token) {

						prev_token = makeToken(Character.toString(current), token_id++, line_number, offset);
						token_list.add(prev_token);
					}
					++offset;
				}

				else if (!string_sequence && (current == ' ' || current == '\t' || current == '\n' || current == '\r' || current == ';')) {

					if (current == '\t')
						offset += 4;
					else
						++offset;
					if (prev_token != null && prev_token.getKind() == TokenKind.OPEN_PARENTHESIS && current != ';') {

						current = (char) fis.read();
						continue;
					}

					reading_token = false;

					if (buffer.length() > 0) {
						
						prev_token = makeToken(buffer.toString(), token_id++, line_number, offset);
						token_list.add(prev_token);
						buffer = new StringBuilder("");
						if (current == ';') {

							buffer.append(current);
							current = (char) fis.read();							
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

					if (current != '(')
						reading_token = true;
					continue;
				}

				buffer.append(current);
				++offset;
				reading_token = true;
				current = (char) fis.read();
			}

			buffer.append(current);
			current = (char) fis.read();
			++offset;
		}

		catch (IOException e) {

			e.printStackTrace();
		}

		if (buffer.length() > 0) {

			prev_token = makeToken(buffer.toString(), token_id++, line_number, offset);
			token_list.add(prev_token);
		}

		return token_list;
	}

	public static void init() {}

    public static void main(String[] args) {
        
    	init();
    	// System.out.println("Working Directory = " + System.getProperty("user.dir"));

        // Lexical Analysis
    	System.out.println("Starting Lexical Analysis...");
        String filename = INPUT_DIRECTORY + "test5.drl";
    	List<Token> token_list = lexer(filename);
    	System.out.println("List of all tokens: ");
    	for (Token x : token_list)
    		System.out.println(x.getKind() + "\t" + x.getName() + ", " + x.getLineNumber() + ", " + x.getOffset());
    	System.out.println("Lexical analysis complete...!!!\n");
    	
        // Parsing
    	System.out.println("Starting Parser...");
        AbsSynTree ast = parser(token_list, filename);
        ast.preorder();
        System.out.println("Parsing complete...!!!\n");

        // Intermediate Code Generation

        // Optimization: Currently expanding the Expression nodes
        System.out.println("Optimizing the Parse Tree...");
        Optimizer.optimize(ast);
        System.out.println("Complete...!!!\n");

        // Code generation : Converting DRLang code to Assembly
        System.out.println("Writing Assembly Code...");
        String output_filepath = OUTPUT_DIRECTORY + "test5.s";
        generate(ast, output_filepath);
        System.out.println("Assembly generated!!!");
        System.out.println("Output Location: " + new File(output_filepath).getAbsolutePath() + "\n");
    }
}
