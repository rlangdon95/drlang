package com.my.compiler.drlang;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {

	private T data;

	private int level;

	// index location of the node in the parent's list of children
	private int index;

	private List<Node<?>> children;

	private Node<?> parent;

	private Node<?> next_child;

	private NodeKind kind;
	
	public Node(T data, NodeKind kind) {

		this.data = data;
		this.level = -1;
		this.index = 0;
		this.children = new ArrayList<Node<?>>();
		this.parent = null;
		this.next_child = null;
		this.kind = kind;
	}

	public T getData() { return this.data; }

	public int getLevel() { return this.level; }

	public int getIndex() { return this.index; }

	public List<Node<?>> getChildren() { return this.children; }

	public Node<?> getParent() { return this.parent; }

	public NodeKind getKind() { return this.kind; }

	public void setData(T data) { this.data = data; }

	public void setLevel(int level) { this.level = level; }

	public void setIndex(int index) { this.index = index; } 

	public void setParent(Node<?> parent) { this.parent = parent; this.level = parent.getLevel(); }

	@Override
	public boolean equals(Object node) { return (this.hashCode() == (node.hashCode())); }

	public void addChild(Node<?> node) {

		if (node == null)
			return;

		node.setParent(this);
		node.setLevel(this.level + 1);
		node.setIndex(this.children.size());

		this.children.add(node);
		if (this.next_child == null)
			this.next_child = node;
	}

	public Node<?> getNextChild() { return this.next_child; }

	public void preorder() { preorder(this, 0); }

	private void preorder(Node<?> root, int indent) {

		if (root == null)
			return;

		for (int i = 0; i < indent; ++i) System.out.print(" ");
		System.out.print(root.getKind() + ": " + root.getData());
		System.out.print("\n");

		List<Node<?>> children = root.getChildren();
		for (Node<?> x : children)
			preorder(x, indent + 1);
	}

	public boolean isLeaf() {

		return ((this.children.size() == 0) || (this.children == null));
	}
}