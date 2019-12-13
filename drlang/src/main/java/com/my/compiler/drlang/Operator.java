package com.my.compiler.drlang;

import java.util.Comparator;

public class Operator implements Comparable<Operator> {

	private String symbol;

	private OperatorKind kind;

	private OperatorPrecedence precedence;

	public Operator(String symbol) {

		this.symbol = symbol;
		switch (symbol) {

			case "+":
			this.kind = OperatorKind.PLUS_OP;
			this.precedence = OperatorPrecedence.PLUS;
			break;

			case "-":
			this.kind = OperatorKind.MINUS_OP;
			this.precedence = OperatorPrecedence.MINUS;
			break;

			case "*":
			this.kind = OperatorKind.MULTIPLY_OP;
			this.precedence = OperatorPrecedence.MULTIPLY;
			break;

			case "/":
			this.kind = OperatorKind.DIVISION_OP;
			this.precedence = OperatorPrecedence.DIVISION;
			break;

			case "^":
			this.kind = OperatorKind.EXPONENT_OP;
			this.precedence = OperatorPrecedence.EXPONENT;
			break;
		}
	}

	public String getSymbol() { return this.symbol; }

	public OperatorKind getKind() { return this.kind; }

	public OperatorPrecedence getPrecedence() { return this.precedence; }

	@Override
	public int compareTo(Operator op) {
		
		return this.getKind().ordinal() - op.getKind().ordinal();
	}
}