package com.my.compiler.drlang;

public class TokenPattern {

	private String name;

	private TokenKind kind;

	public TokenPattern(String name, TokenKind kind) {

		this.name = name;
		this.kind = kind;
	}

	public String getName() { return this.name; }

	public TokenKind getKind() { return this.kind; }
}