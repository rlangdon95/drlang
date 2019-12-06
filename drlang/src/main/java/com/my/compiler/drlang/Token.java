package com.my.compiler.drlang;

public class Token implements Comparable<Token> {

	private String name;

	private int id;

	private int line_number;

	private int offset;

	private TokenKind kind;

	public Token(String name, int id, int line_number, int offset, TokenKind kind) {

		this.name = name;
		this.id = id;
		this.line_number = line_number;
		this.offset = offset;
		this.kind = kind;
	}

	public Token(String name, int id, int line_number, TokenKind kind) {

		this.name = name;
		this.id = id;
		this.line_number = line_number;
		this.offset = -1;
		this.kind = kind;
	}

	public String getName() { return this.name; }

	public int getId() { return this.id; }

	public int getLineNumber() { return this.line_number; }

	public int getOffset() { return this.offset; }

	public TokenKind getKind() { return this.kind; }

	@Override
	public int compareTo(Token token) {

		return this.name.compareTo(token.getName());
	}

	@Override
	public int hashCode() {

		return this.name.hashCode();
	}

	public boolean equals(Token token) {

		return this.name.equals(token.getName()) &&
			   this.id == token.getId() &&
			   this.kind == token.getKind();
	}
}