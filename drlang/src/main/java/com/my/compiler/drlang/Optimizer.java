package com.my.compiler.drlang;

import java.util.LinkedList;
import java.util.Queue;

public class Optimizer {

	public static void optimize(AbsSynTree ast) {

		Node<?> root = ast.getRoot();
		Queue<Node<?>> queue = new LinkedList<Node<?>>();
		queue.add(root);
		
		while (!queue.isEmpty()) {
			
			Node<?> node = queue.peek();
			if (node.getKind() == NodeKind.EXPRESSION) {

				int index = node.getIndex();
				Node<?> parent = node.getParent();
				Node<?> save = Parser.createExpressionTree(node);

				// remove the current expression node
				parent.getChildren().remove(index);

				// replace the expression node with the syntax tree
				parent.getChildren().add(index, save);
			}
			
			for (Node<?> x : node.getChildren())
				queue.add(x);
			
			queue.poll();
		}
	}
}