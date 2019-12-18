package com.my.compiler.drlang;

public enum NodeKind {

	// default or no kind
	NOKIND,

	// Program level kinds
	PROGRAM,
	FUNCTION,

	// Brackets
	LEFT_PARENTHESIS,
	RIGHT_PARENTHESIS,

	// Binary Arithmetic Operators
	PLUS_OP,
	MINUS_OP,
	MULTIPLY_OP,
	DIVISION_OP,
	EXPONENT_OP,

	// Unary Operators
	NOT_OP,
	NEGATE_OP,
	POSITIVE_OP,
	COMPLEMENT_OP,

	// Logical Operators
	OR_OP,
	AND_OP,
	XOR_OP,
	EQUALS_OP,
	NOTEQUALS_OP,

	// Expressions
	EXPRESSION,

	// Assignment Operator
	ASSIGN_OP,

	// Function return
	RETURN,

	// Constant literals
	LITERAL,

	// Separators
	SEMICOLON
}