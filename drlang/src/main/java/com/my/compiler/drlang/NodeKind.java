package com.my.compiler.drlang;

public enum NodeKind {

	STATEMENT,

	// Binary Arithmetic Operators
	PLUS_OP,
	MINUS_OP,
	MULTIPLY_OP,
	DIVISION_OP,

	// Unary Operators
	NOT_OP,
	NEGATION_OP,
	COMPLEMENT_OP,

	// Logical Operators
	OR_OP,
	AND_OP,
	XOR_OP,
	EQUALS_OP,
	NOTEQUALS_OP,

	// Assignment Operator
	ASSIGN_OP,

	RETURN
}