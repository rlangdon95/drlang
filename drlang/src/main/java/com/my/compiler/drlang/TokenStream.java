package com.my.compiler.drlang;

import java.util.List;

public class TokenStream {

	private int index;

	private int size;

	private List<Token> list;

	public Token tok;

	public TokenStream(List<Token> list) {

		this.index = 0;
		this.size = list.size();
		this.list = list;
		this.tok = list.get(this.index);
	}

	public boolean hasNext() { return (this.index < size); }

	public Token next() {

		if (index >= size)
			throw new IndexOutOfBoundsException("No more tokens in token stream.");

		this.tok = this.list.get(index);
		++index;
		return this.tok;
	}
	
	@Override
	public String toString() {
		
		StringBuilder ret = new StringBuilder("Index: " + Integer.toString(index) + "\n");
		
		for (Token x : list)
			ret.append(x.toString() + "\n");
		
		return ret.toString();
	}
	
	public List<Token> getList() { return this.list; }
}