package com.my.compiler.drlang;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class AbsSynTree extends Tree {

	private Node<?> root;

	public AbsSynTree() {

		root = null;
	}

	public Node<?> getRoot() { return root; }

	// traverse the tree in a level order and when the parent is found, add node as child and return true
	// return false, otherwise
	public boolean add(Node<?> node, Node<?> parent) {

		if (this.root == null || parent == null) {

			this.root = node;
			node.setLevel(0);
			return true;
		}

		Queue<Node<?>> queue = new LinkedList<Node<?>>();
		queue.add(this.root);

		while (!queue.isEmpty()) {

			List<Node<?>> children = queue.peek().getChildren();
			for (Node<?> x : children)
				queue.add(x);

			if (parent.getKind() == NodeKind.RETURN &&
				  node.getKind() == NodeKind.EXPRESSION) {

				if (queue.peek() != null)
					System.out.println(queue.peek().getKind());
			}

			if (parent.equals(queue.peek())) {
		
				parent.addChild(node);
				return true;
			}

			queue.poll();
		}

		return false;
	}

	public void inorder() { inorder(this.root); }

	public void preorder() { preorder(this.root, 0L); }

	public void postorder() { postorder(this.root, 0L); }

	public void levelorder() { levelorder(this.root, -1L); }

	private void inorder(Node<?> node) {

		if (node == null)
			return;
	}

	private void preorder(Node<?> node, long indent) {

		if (node == null)
			return;

		System.out.print((indent + 1) + ":");
		for (int i = 0; i < indent; ++i) System.out.print(" ");
		System.out.print(node.getKind() + ": " + node.getData());
		System.out.print("\n");

		List<Node<?>> children = node.getChildren();
		for (Node<?> x : children)
			preorder(x, indent + 1);
	}

	private void postorder(Node<?> node, long indent) {

		if (node == null)
			return;

		List<Node<?>> children = node.getChildren();
		for (Node<?> x : children)
			preorder(x, indent + 1);
		
		System.out.print((indent + 1) + ":");
		for (int i = 0; i < indent; ++i) System.out.print(" ");
		System.out.print(node.getData());
		System.out.print("\n");
	}

	@SuppressWarnings("rawtypes")
	private void levelorder(Node<?> node, long level) {

		Queue<Node> queue = new LinkedList<Node>();
		queue.add(node);

		while (!queue.isEmpty()) {

			List<Node<?>> children = node.getChildren();
			for (Node<?> x : children)
				queue.add(x);

			if (level < 0 || level == queue.peek().getLevel())
				System.out.println(queue.peek().getData());
			queue.poll();
		}
	}
}