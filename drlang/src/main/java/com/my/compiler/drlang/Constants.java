package com.my.compiler.drlang;

public class Constants {

	/* ********************************************************************************************************** */
	/* ASSEMBLY KEYWORDS */
	/* ********************************************************************************************************** */
	// movl moves a long (32-bits) from source to destination
	public static final String MOVEL = "movl";

	// movl moves a long (32-bits) from source to destination
	public static final String MOVEQ = "movq";

	// return from procedure
	public static final String RET = "ret";

	// negate the value in a register
	public static final String NEG = "neg";
	/* ********************************************************************************************************** */
	/* ASSEMBLY KEYWORDS */
	/* ********************************************************************************************************** */


	/* ********************************************************************************************************** */
	/* ASSEMBLY REGISTERS */
	/* ********************************************************************************************************** */
	// Extended Accumulator Register
	public static final String EAX = "eax";
	/* ********************************************************************************************************** */
	/* ASSEMBLY REGISTERS */
	/* ********************************************************************************************************** */


	/* ********************************************************************************************************** */
	/* String constants */
	/* ********************************************************************************************************** */
	// newline
	public static final String LINEFEED = "\n";

	// carriage return
	public static final String CARRIAGE_RETURN = "\r";
	/* ********************************************************************************************************** */
	/* String constants */
	/* ********************************************************************************************************** */


	/* ********************************************************************************************************** */
	/* Token patterns */
	/* ********************************************************************************************************** */
	// TODO: Refactor these
	public static final TokenPattern INT = new TokenPattern("int", TokenKind.INT);

	public static final TokenPattern OPEN_PARENTHESIS = new TokenPattern("\\(", TokenKind.OPEN_PARENTHESIS);

	public static final TokenPattern CLOSE_PARENTHESIS = new TokenPattern("\\)", TokenKind.CLOSE_PARENTHESIS);

	public static final TokenPattern OPEN_CURLY = new TokenPattern("\\{", TokenKind.OPEN_CURLY);

	public static final TokenPattern CLOSE_CURLY = new TokenPattern("\\}", TokenKind.CLOSE_CURLY);

	public static final TokenPattern SEMICOLON = new TokenPattern(";", TokenKind.SEMICOLON);

	public static final TokenPattern RETURN = new TokenPattern("ret", TokenKind.RETURN);

	public static final TokenPattern IDENTIFIER = new TokenPattern("([a-zA-Z][a-zA-Z0-9_$]*)", TokenKind.IDENTIFIER);

	public static final TokenPattern IDENTIFIER_FUNCTION = new TokenPattern("([a-zA-Z][a-zA-Z0-9_$]*\\(\\))", TokenKind.IDENTIFIER_FUNCTION);

	public static final TokenPattern LITERAL_INT = new TokenPattern("[0-9]+", TokenKind.LITERAL_INT);

	// Arithmetic expressions
	public static final TokenPattern EXPRESSION = new TokenPattern("\\(*[0-9]+\\)*", TokenKind.EXPRESSION);
	// Arithmetic expressions

	// Unary Operators
	public static final TokenPattern NEGATION = new TokenPattern("-", TokenKind.NEGATION);

	public static final TokenPattern COMPLEMENT = new TokenPattern("~", TokenKind.COMPLEMENT);

	public static final TokenPattern LOGICAL_NEGATION = new TokenPattern("!", TokenKind.LOGICAL_NEGATION);
	// Unary Operators

	public static final TokenPattern KEYWORDS[] = {	INT,
													OPEN_PARENTHESIS,
													CLOSE_PARENTHESIS,
													OPEN_CURLY,
													CLOSE_CURLY,
													SEMICOLON,
													RETURN,
													IDENTIFIER,
													IDENTIFIER_FUNCTION,
													LITERAL_INT,
													EXPRESSION,
													NEGATION,
													COMPLEMENT,
													LOGICAL_NEGATION
												  };
	/* ********************************************************************************************************** */
	/* Token patterns */
	/* ********************************************************************************************************** */
}