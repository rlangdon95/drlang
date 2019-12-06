package com.my.compiler.drlang;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {

	private T data;

	private int level;

	private List<Node<?>> children;

	private Node<?> parent;

	private Node<?> next_child;
	
	public Node(T data) {

		this.data = data;
		this.level = -1;
		this.children = new ArrayList<Node<?>>();
		this.parent = null;
		this.next_child = null;
	}

	public T getData() { return this.data; }

	public int getLevel() { return this.level; }

	public List<Node<?>> getChildren() { return this.children; }

	public Node<?> getParent() { return this.parent; }

	public void setData(T data) { this.data = data; }

	public void setLevel(int level) { this.level = level; }

	public void setParent(Node<?> parent) { this.parent = parent; this.level = parent.getLevel(); }

	public void addChild(Node<?> node) {

		this.children.add(node);
		if (this.next_child == null)
			this.next_child = node;
	}

	public Node<?> getNextChild() { return this.next_child; }
}