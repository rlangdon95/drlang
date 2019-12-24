package com.my.compiler.drlang;

import java.util.List;

public class Statement {

	private List<Token> tokens;

	private StatementKind kind;

	public Statement(List<Token> tokens) {

		this.tokens = tokens;
		this.kind = StatementKind.NOKIND;
	}

	public List<Token> getTokens() { return this.tokens; }

	public StatementKind getKind() { return this.kind; }

	public void setKind(StatementKind kind) { this.kind = kind; }

	public String toString() {

		StringBuilder stmt = new StringBuilder("");
		for (Token x : tokens) {

			stmt.append(x.getName());
			stmt.append(" ");
		}

		return stmt.toString();
	}
}