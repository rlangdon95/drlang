package com.my.compiler.drlang;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {

	private T data;

	private List<Node> children;
	
	public Node(T data) {

		this.data = data;
		this.children = new ArrayList<Node>();
	}

	public List<Node> getChildren() { return this.children; }

	public T getData() { return this.data; }
}