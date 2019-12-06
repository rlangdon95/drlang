package com.my.compiler.drlang;

public class Function {

	private String name;

	private int id;

	private FunctionReturnKind kind;

	public Function(String name, int id, FunctionReturnKind kind) {

		this.name = name;
		this.id = id;
		this.kind = kind;
	}

	public String getName() { return this.name; }

	public int getId() { return this.id; }

	public FunctionReturnKind getKind() { return this.kind; }
}