package com.my.compiler.drlang;

public class Constants {

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

	public static final TokenPattern KEYWORDS[] = {	INT,
													OPEN_PARENTHESIS,
													CLOSE_PARENTHESIS,
													OPEN_CURLY,
													CLOSE_CURLY,
													SEMICOLON,
													RETURN,
													IDENTIFIER,
													IDENTIFIER_FUNCTION,
													LITERAL_INT
												  };
}