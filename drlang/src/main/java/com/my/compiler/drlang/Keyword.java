package com.my.compiler.drlang;

public class Keyword {

	private String word;

	private TokenKind kind;

	public Keyword(String word, TokenKind kind) {

		this.word = word;
		this.kind = kind;
	}

	public String getWord() { return this.word; }

	public TokenKind getKind() { return this.kind; }
}