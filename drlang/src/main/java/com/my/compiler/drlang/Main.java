package com.my.compiler.drlang;

import java.io.IOException;
import java.io.OutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
	
	public static Set<Variable> var_set = new HashSet<Variable>();
	
	public static int program_scope = 0;
	
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
				line.append("()");
				lines.add(line.toString());

				line = new StringBuilder("_");
				line.append(function_name);
				line.append("()");
				lines.add(line.toString());
				for (Node<?> x : (List<Node<?>>)node.getChildren())
					generate(x, root, lines);
			}
			break;

			case RETURN:
			{
				boolean negate = false;
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
				
				if (negate) {
					
					line.append("\t");
					line.append(Constants.NEG);
					line.append("\t");
					line.append(Constants.EAX);
					lines.add(line.toString());
				}

				line = new StringBuilder("\t");
				line.append(Constants.RET);
				lines.add(line.toString());
			}
			break;
			
			case STATEMENT:
			{
				if (node.isLeaf()) {
					
					System.err.println("Parsing was not done or was incomplete. Exiting.");
					System.exit(255);
				}
				
				for (Node x : node.getChildren())
					generate(x, node, lines);
			}
			break;

			case SEMICOLON:
			{
				lines.add(Constants.LINEFEED);
			}
			break;
		default:
			System.err.println("Bad Node Kind. Exiting. " + node.getKind().toString());
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

				if (((Function) x.getData()).getName().equals("main")) {

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
	
	public static void addStatements(List<Token> token_list, Node<?> root) {

		// number of valid tokens detected
		int number_of_tokens = token_list.size();

		// expecting function
		boolean expecting_function = true;

		// last function that was inserted into the syn tree
		Node<Function> function_node = null;

		// function object reference
		Function func = null;

		// function id
		int function_id = 0;

		// function return kind
		FunctionReturnKind retkind = FunctionReturnKind.NOKIND;

		// flag to denote if we are expecting function arguments
		boolean arguments = false;

		// flag to denote if we are present inside function definition
		boolean inside_function = false;

		// keep count of curly braces
		int curly_count = 0;

		// current statement: collection of tokens
		List<Token> statement = null;

		for (int i = 0; i < number_of_tokens; ++i) {

			Token token = token_list.get(i);
			Token prev_token = (i == 0) ? null : token_list.get(i - 1);

			switch (token.getKind()) {

				case INT:
					if (expecting_function && (retkind != FunctionReturnKind.NOKIND)) {

						System.err.println("Multiple return kinds for a function not supported yet. Exiting.");
						System.exit(255);
					}

					if (expecting_function)
						retkind = FunctionReturnKind.INT;
					else
						statement.add(token);
					break;

				case IDENTIFIER:
					if (expecting_function)
						func = new Function(token.getName(), function_id++, retkind);
					else
						statement.add(token);
					break;

				case OPEN_PARENTHESIS:
					if (expecting_function)
						arguments = true;

					else {}
					break;

				case CLOSE_PARENTHESIS:
					if (expecting_function) {

						arguments = false;
						function_node = new Node<Function>(func, NodeKind.FUNCTION);
						root.addChild(function_node);
					}
					break;

				case OPEN_CURLY:
					++curly_count;
					if (expecting_function) {

						expecting_function = false;
						inside_function = true;
						statement = new ArrayList<Token>();
					}
					break;

				case CLOSE_CURLY:
					--curly_count;
					if (curly_count < 0) {
					
						System.err.println("Unmatched closing brace found. Exiting.");
						System.exit(255);
					}

					else if (curly_count == 0) {

						inside_function = false;
						expecting_function = true;
					}
					break;

				case SEMICOLON:
					statement.add(token);
					Statement stmt = new Statement(statement);
					Node<Statement> stmt_node = new Node<Statement>(stmt, NodeKind.STATEMENT);
					function_node.addChild(stmt_node);
					statement = new ArrayList<Token>();
					break;
				
				case RETURN:
				case LITERAL_INT:
					
				// Assignment operator
				case ASSIGNMENT_OP:
					
				// Binary arithmetic operators
				case PLUS_OP:
				case MINUS_OP:
				case DIVISION_OP:
				case MULTIPLY_OP:
				
				// Logical operators
				case AND_OP:
				case OR_OP:
					
				// Relational operators
				case EQUALS_OP:
				case GREATER_EQUALS_OP:
				case GREATER_OP:
				case LESSER_EQUALS_OP:
				case LESSER_OP:
				case NOT_EQUALS_OP:
				
				// Unary operators
				case NEGATION:
					statement.add(token);
					break;
				
				case COMMA:
				case COMPLEMENT:
				case EXPRESSION:
				case IDENTIFIER_FUNCTION:
				case LOGICAL_NEGATION:
				case TERM:
					System.err.println(token.getKind() + " is not supported yet. Exiting.");
					System.exit(255);
					break;
					
				default:
					System.err.println("Unexpected token kind found during parsing. Exiting.");
					System.exit(255);
					break;
			}
		}
	}
	
	public static void parse(Node<?> root) {	
	
		if (root == null)
			return;
		
		NodeKind nk = root.getKind();
		switch (nk) {
		
		case PROGRAM:
		case FUNCTION:
		{
			for (Node<?> x : root.getChildren())
				parse(x);
		}
		break;
		
		case STATEMENT:
		{
			Statement stmt = (Statement) root.getData();
			TokenStream ts = new TokenStream(stmt.getTokens());
			Grammar grammar = new Grammar(ts, root);
			grammar.parse();
		}
		break;
		}
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
		
		boolean identifier = false;

		// current token
		Token token = null;

		try (FileInputStream fis = new FileInputStream(file)) {				

			while (fis.available() > 0) {
				
				current = (char) fis.read();

				if (string_sequence) {
					
					buffer.append(current);
					++offset;
					if (fis.available() > 0)
						current = (char) fis.read();
					else
						break;
					continue;
				}
				
				switch (current) {
				
				case '\"':
				case '#':
				case '$':
				case '\'':
				case ',':
				case ':':
				case '@':
				case '\\':
				case '`':

				case '%':
				case '&':
				case '.':
				case '<':
				case '=':
				case '>':
				case '?':
				case '[':
				case ']':
				case '~':
					if (!string_sequence) {

						System.err.println("Unexpected character " + current + " at line " + line_number + ", column " + offset + ". Exiting.");
						System.exit(255);
					}
					break;

				case '(':
				case ')':
				case '*':
				case '+':
				case '-':
				case '/':
				case ';':
				case '!':
				case '^':
				case '|':
					if (buffer.length() > 0) {

						identifier = false;
						token = CompilerUtils.makeToken(buffer, token_id++, line_number, offset);
						token_list.add(token);
						buffer = new StringBuilder("");
					}

					token = CompilerUtils.makeToken(Character.toString(current), token_id++, line_number, offset);
					token_list.add(token);
					++offset;
					break;

				case '_':
					if (!identifier && !string_sequence) {
						
						System.err.println("Unexpected character " + current + " at line " + line_number + ", column " + offset + ". Exiting.");
						System.exit(255);
					}
					
					++offset;
					buffer.append(current);
					break;


				case '{':
				case '}':
					if (buffer.length() > 0) {

						identifier = false;
						token = CompilerUtils.makeToken(buffer, token_id++, line_number, offset);
						token_list.add(token);
						buffer = new StringBuilder("");
					}

					token = CompilerUtils.makeToken(Character.toString(current), token_id++, line_number, offset);
					token_list.add(token);
					++offset;
					program_scope += (current == '{') ? 1 : (-1);
					break;

				case ' ':
				case '\t':
				case '\n':
				case '\r':
					if (buffer.length() > 0) {

						identifier = false;
						token = CompilerUtils.makeToken(buffer, token_id++, line_number, offset);
						token_list.add(token);
						buffer = new StringBuilder("");
					}

					++offset;
					if (current == '\t')
						offset += 3;

					else if (current == '\n') {

						offset = 0;
						++line_number;
					}
					
					else if (current == '\r')
						offset = 0;
					
					break;

				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':	
					buffer.append(current);
					++offset;
					break;
				
				case 'A':
				case 'B':
				case 'C':
				case 'D':
				case 'E':
				case 'F':
				case 'G':
				case 'H':
				case 'I':
				case 'J':
				case 'K':
				case 'L':
				case 'M':
				case 'N':
				case 'O':
				case 'P':
				case 'Q':
				case 'R':
				case 'S':
				case 'T':
				case 'U':
				case 'V':
				case 'W':
				case 'X':
				case 'Y':
				case 'Z':

				case 'a':
				case 'b':
				case 'c':
				case 'd':
				case 'e':
				case 'f':
				case 'g':
				case 'h':
				case 'i':
				case 'j':
				case 'k':
				case 'l':
				case 'm':
				case 'n':
				case 'o':
				case 'p':
				case 'q':
				case 'r':
				case 's':
				case 't':
				case 'u':
				case 'v':
				case 'w':
				case 'x':
				case 'y':
				case 'z':
					identifier = true;
					++offset;
					buffer.append(current);
					break;

				default:
					if (!string_sequence) {

						System.err.println("Unexpected character " + current + " at line " + line_number + ", column " + offset + ". Exiting.");
						System.exit(255);
					}
					break;
				}
			}
		}

		catch (IOException e) {

			e.printStackTrace();
		}

		return token_list;
	}

	public static AbsSynTree parser(List<Token> token_list, String filename) {

		AbsSynTree tree = new AbsSynTree();
		Node<Program> root = new Node<Program>(new Program(filename), NodeKind.PROGRAM);
		tree.add(root, null);
		
		addStatements(token_list, root);
		
		parse(root);
		return tree;
	}
	
	public static void init() {}

    public static void main(String[] args) {
        
    	init();
    	// System.out.println("Working Directory = " + System.getProperty("user.dir"));

        // Lexical Analysis
    	System.out.print("Starting Lexical Analysis... ");
        String filename = "test1.drl";
    	List<Token> token_list = lexer(INPUT_DIRECTORY + filename);
    	System.out.println("Done!!!\n");
    	System.out.println("##########Tokens List##########");
    	CompilerUtils.dispListPretty(token_list);
    	System.out.println("##########Tokens List##########\n");
    	
        // Parsing
    	System.out.print("Starting Parser... ");
        AbsSynTree ast = parser(token_list, filename);
        System.out.println("Done!!!\n");
        System.out.println("##########Syntax Tree##########");
        ast.preorder();
        System.out.println("##########Syntax Tree##########\n");
        
        // Intermediate Code Generation

        /*
        // Optimization: Currently expanding the Expression nodes
        System.out.println("Optimizing the Parse Tree...");
        Optimizer.optimize(ast);
        System.out.println("Complete...!!!\n");*/

        // Code generation : Converting DRLang code to Assembly
        System.out.print("Writing Assembly Code... ");
        String output_filepath = "test1.s";
        generate(ast, OUTPUT_DIRECTORY + output_filepath);
        System.out.println("Done!!!\n");
        System.out.println("##########Generated Code##########");
        CompilerUtils.printAssembly(OUTPUT_DIRECTORY + output_filepath);
        System.out.println("##########Generated Code##########\n");
        
        System.out.println("Output Location: " + new File(OUTPUT_DIRECTORY + output_filepath).getAbsolutePath() + "\n");
        
        if (CompilerUtils.checkSolution(OUTPUT_DIRECTORY, output_filepath) > 0)
        	System.err.println("Bad assembly generated.");
        else
        	System.out.println("Assembly generated correctly.");
    }
}
