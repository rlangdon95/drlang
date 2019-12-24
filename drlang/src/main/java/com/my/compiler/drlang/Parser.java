package com.my.compiler.drlang;

import java.util.Stack;

public class Parser {

	public static NodeKind getNodeKind(String ch) {

		switch (ch) {

			case "+":
				return NodeKind.PLUS_OP;

			case "-":
				return NodeKind.MINUS_OP;

			case "*":
				return NodeKind.MULTIPLY_OP;

			case "/":
				return NodeKind.DIVISION_OP;

			case "^":
				return NodeKind.EXPONENT_OP;
				
			case "(":
				return NodeKind.LEFT_PARENTHESIS;
				
			case ")":
				return NodeKind.RIGHT_PARENTHESIS;

			default:
				System.err.println("Bad expression. Exiting.");
				System.exit(255);
		}

		return NodeKind.NOKIND;
	}

	public static NodeKind getNodeKind(char ch) {

		switch (ch) {

			case '+':
				return NodeKind.PLUS_OP;

			case '-':
				return NodeKind.MINUS_OP;

			case '*':
				return NodeKind.MULTIPLY_OP;

			case '/':
				return NodeKind.DIVISION_OP;

			case '^':
				return NodeKind.EXPONENT_OP;
				
			case '(':
				return NodeKind.LEFT_PARENTHESIS;
				
			case ')':
				return NodeKind.RIGHT_PARENTHESIS;

			default:
				System.err.println("Bad expression. Exiting.");
				System.exit(255);
		}

		return NodeKind.NOKIND;
	}

	public static Node<?> createExpressionTree(Node<?> node) {

		if (node.getKind() == NodeKind.EXPRESSION)
			return createExpressionTree((String) node.getData());

		System.err.println("Unknown identifier. Exiting.");
		System.exit(255);
		return null;
	}

	public static Node<?> createExpressionTree(String exp) {

		char[] arr = exp.toCharArray();
		return createExpressionTree(arr, 0, arr.length);
	}
	
	// NOTE: high = last_legal_index + 1
	public static Node<?> createExpressionTree(char[] arr, int low, int high) {

		Node<?> node = null;
		Stack<Node<?>> operand = new Stack<Node<?>>();
		Stack<Operator> operator = new Stack<Operator>();
		StringBuilder term = new StringBuilder("");
		boolean reading_number = false;
		boolean is_negative = false;
		boolean negate_expression = false;
		NodeKind prev_node_kind = NodeKind.NOKIND;
		for (int i = low; i < high; ++i) {

			if (low == 0 && i == 0 && arr[i] == '-') {

				// TODO: create a negate node and make it the parent of rest of the expression tree
				negate_expression = !negate_expression;
				continue;
			}

			char ch = arr[i];
			switch (ch) {

				case '+':
				case '-':
				case '*':
				case '/':
				case '^':
				{
					if (reading_number) {

						// add the number to the operand stack
						Node<String> number = new Node<String>(term.toString(), NodeKind.LITERAL);
						if (!operand.isEmpty() && operand.peek().getKind() == NodeKind.POSITIVE_OP) {

							operand.pop();
							operand.push(number);
						}

						else if (!operand.isEmpty() && operand.peek().getKind() == NodeKind.NEGATE_OP)
							operand.peek().addChild(number);

						else
							operand.push(number);

						// reset term
						term = new StringBuilder("");
						prev_node_kind = NodeKind.LITERAL;
						reading_number = false;
					}

					if (ch == '-' || ch == '+') {

						// TODO: Handle negative values
						if (prev_node_kind == NodeKind.PLUS_OP ||
							prev_node_kind == NodeKind.MINUS_OP ||
							prev_node_kind == NodeKind.MULTIPLY_OP ||
							prev_node_kind == NodeKind.DIVISION_OP ||
							prev_node_kind == NodeKind.LEFT_PARENTHESIS ||
							prev_node_kind == NodeKind.RIGHT_PARENTHESIS) {
							
							is_negative = true;
							
							Node<String> sign = (ch == '-') ? new Node<String>(Character.toString(ch), NodeKind.NEGATE_OP)
									                        : new Node<String>(Character.toString(ch), NodeKind.POSITIVE_OP);
							if ((operand.peek().getKind() != NodeKind.NEGATE_OP) && (operand.peek().getKind() != NodeKind.POSITIVE_OP))
								operand.push(sign);
							else
								operand.peek().addChild(sign);
						}
					}
					prev_node_kind = getNodeKind(ch);
					Operator op = new Operator(Character.toString(ch));
					
					while ((operand.size() > 1) && (op.getKind().compareTo(operator.peek().getKind()) < 0)) {

						Node<String> temp = new Node<String>(operator.peek().getSymbol(), prev_node_kind);
						Node<?> lhs = operand.pop();
						Node<?> rhs = operand.pop();
						temp.addChild(rhs);
						temp.addChild(lhs);
						
						operator.pop();
						operand.push(temp);
						node = temp;
					}

					operator.push(op);
				}
				break;

				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
				{
					reading_number = true;
					term.append(ch);
					prev_node_kind = NodeKind.LITERAL;
				}
				break;

				case '(':
				{
					if (reading_number) {

						Node<String> op = new Node<String>(term.toString(), NodeKind.LITERAL);
						if (!operand.isEmpty() && operand.peek().getKind() == NodeKind.POSITIVE_OP) {

							operand.pop();
							operand.push(op);
						}

						else if (!operand.isEmpty() && operand.peek().getKind() == NodeKind.NEGATE_OP)
							operand.peek().addChild(op);

						else
							operand.push(op);

						term = new StringBuilder("");
						reading_number = false;
					}

					Operator op = new Operator(Character.toString(ch));
					operator.push(op);
				}
				break;

				case ')':
				{
					if (reading_number) {
						
						Node<String> op = new Node<String>(term.toString(), NodeKind.LITERAL);
						if (!operand.isEmpty() && operand.peek().getKind() == NodeKind.POSITIVE_OP) {

							operand.pop();
							operand.push(op);
						}

						else if (!operand.isEmpty() && operand.peek().getKind() == NodeKind.NEGATE_OP)
							operand.peek().addChild(op);

						else
							operand.push(op);

						term = new StringBuilder("");
						reading_number = false;
					}
					
					do {
						
						if (operator.size() == 0) {
							
							System.err.println("Unbalanced parenthesis. Exiting.");
							System.exit(255);
						}
						
						String _operator = operator.pop().getSymbol();
						
						// TODO: need to handle this case better and more rigorously
						// NB: Only because this condition seems off to me
						if (operand.size() <= 1)
							break;
						
						Node<String> temp = new Node<String>(_operator, getNodeKind(_operator));
						
						if (temp.getKind() == NodeKind.LEFT_PARENTHESIS)
							break;
						
						Node<?> lhs = operand.pop();
						Node<?> rhs = operand.pop();
						temp.addChild(rhs);
						temp.addChild(lhs);
						
						operand.push(temp);
						node = temp;
					} while (true);
				}
				break;

				case ' ':
				{
					if (reading_number) {
					
						Node<String> number = new Node<String>(term.toString(), NodeKind.LITERAL);
						if (!operand.isEmpty() && operand.peek().getKind() == NodeKind.POSITIVE_OP) {

							operand.pop();
							operand.push(number);
						}

						else if (!operand.isEmpty() && operand.peek().getKind() == NodeKind.NEGATE_OP)
							operand.peek().addChild(number);

						else
							operand.push(number);
						
						term = new StringBuilder("");
						reading_number = false;
					}
				}
				continue;
				
				default:
				System.err.println("Bad Arithmetic Expression. Exiting. " + (int)ch);
				System.exit(255);
			}
		}
		
		if (term != null && term.length() > 0)
			operand.push(new Node<String>(term.toString(), NodeKind.LITERAL));
		
		while (operand.size() > 1) {

			String _operator = operator.pop().getSymbol();
			
			Node<String> temp = new Node<String>(_operator, getNodeKind(_operator));
			Node<?> lhs = operand.pop();			
			Node<?> rhs = operand.pop();
			
			temp.addChild(rhs);
			temp.addChild(lhs);
			
			operand.push(temp);
			node = temp;
		}
		
		if (node == null)
			node = operand.pop();
		
		if (!operator.isEmpty() || (operator.size() != 0)) {
			
			System.err.println("Bad Arithmetic Expression. Exiting.");
			System.exit(255);
		}
		
		if (negate_expression) {
			
			Node<String> neg = new Node<String>("-", NodeKind.NEGATE_OP);
			neg.addChild(node);
			node = neg;
		}
		
		Node<String> root = new Node<String>(String.valueOf(arr), NodeKind.EXPRESSION);
		root.addChild(node);
		
		return root;
	}

	public static void main(String[] args) {
		
		Node<?> node = createExpressionTree("20 + 30 --10");
	}
}