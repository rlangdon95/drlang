package com.my.compiler.drlang;

public class Function {

	private String name;

	private int id;

	public Function(String name, int id) {

		this.name = name;
		this.id = id;
	}

	public String getName() { return this.name; }

	public int getId() { return this.id; }
}