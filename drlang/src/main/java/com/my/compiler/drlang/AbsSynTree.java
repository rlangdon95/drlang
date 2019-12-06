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
		System.out.print(node.getData());
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

	@SuppressWarnings("unchecked")
	public static AbsSynTree createSynTree(Token ... token) {

		AbsSynTree tree = new AbsSynTree();
		Node<Token> last_inserted = null;
		for (Token x : token) {

			if (tree.getRoot() == null) {

				Node<Token> root = new Node<Token>(x);
				tree.root = root;
				last_inserted = root;
			}

			else {

				Node<Token> node = new Node<Token>(x);
				last_inserted.addChild(node);
				last_inserted = (Node<Token>)last_inserted.getNextChild();
			}
		}

		return tree;
	}
}