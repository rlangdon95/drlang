package com.my.compiler.drlang;

public abstract class Tree {

	private Node root;

	public abstract void inorder();

	public abstract void preorder();

	public abstract void postorder();

	public abstract void levelorder();
}