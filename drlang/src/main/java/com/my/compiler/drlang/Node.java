package com.my.compiler.drlang;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {

	private T data;

	private int level;

	private List<Node<?>> children;

	private Node<?> parent;

	private Node<?> next_child;

	private NodeKind kind;
	
	public Node(T data, NodeKind kind) {

		this.data = data;
		this.level = -1;
		this.children = new ArrayList<Node<?>>();
		this.parent = null;
		this.next_child = null;
		this.kind = kind;
	}

	public T getData() { return this.data; }

	public int getLevel() { return this.level; }

	public List<Node<?>> getChildren() { return this.children; }

	public Node<?> getParent() { return this.parent; }

	public NodeKind getKind() { return this.kind; }

	public void setData(T data) { this.data = data; }

	public void setLevel(int level) { this.level = level; }

	public void setParent(Node<?> parent) { this.parent = parent; this.level = parent.getLevel(); }

	public boolean equals(Node<?> node) { return (this.data.equals(node.getData()) &&
												 (this.kind == node.getKind())); }

	public void addChild(Node<?> node) {

		this.children.add(node);
		if (this.next_child == null)
			this.next_child = node;
	}

	public Node<?> getNextChild() { return this.next_child; }
}