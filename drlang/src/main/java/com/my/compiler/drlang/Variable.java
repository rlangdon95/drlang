package com.my.compiler.drlang;

public final class Variable {

	// eg: int a = 5;
	// data will store 5
	private final String data;

	// name will store 'a'
	private final String name;

	// kind will store datatype 'int'
	private final VariableKind kind;
	
	// scope
	private final int scope;

	public Variable(String name, TokenKind tkind, int scope) {

		this.name = name;
		this.data = null;
		this.scope = scope;
		switch (tkind) {

			case INT:
				this.kind = VariableKind.INT;
				break;

			default:
				this.kind = VariableKind.NOKIND;
				System.err.println("Bad declaration. Exiting.");
				System.exit(255);
		}
	}

	public Variable(String name, String data, TokenKind tkind, int scope) {

		this.name = name;
		this.data = data;
		this.scope = scope;
		switch (tkind) {

			case INT:
				this.kind = VariableKind.INT;
				break;

			default:
				this.kind = VariableKind.NOKIND;
				System.err.println("Bad declaration. Exiting.");
				System.exit(255);
		}
	}
	
	public String getData() { return this.data; }
	
	public String getName() { return this.name; }
	
	public VariableKind getKind() { return this.kind; }
	
	public int getScope() { return this.scope; }

	@Override
	public boolean equals(Object _obj) {

		if (this == _obj)
			return true;

		if (!(_obj instanceof Variable))
			return false;

		Variable obj = (Variable)_obj;

		return this.name.equals(obj.getName()) &&
			   this.scope == obj.getScope();
	}

	@Override
	public int hashCode() {

		return this.name.hashCode() + ((Integer) this.scope).hashCode();
	}
}