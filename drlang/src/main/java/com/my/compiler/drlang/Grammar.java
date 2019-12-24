package com.my.compiler.drlang;

/**
 * 
 * @author Amartya Majumdar
 * @grammar
 * S -> [ret]E;
 * E -> E[+-/*]F | F
 * F -> [0-9] | $
 */
public class Grammar {

	private TokenStream ts;
	
	private Node<?> root;

	public Grammar(TokenStream ts, Node<?> root) {

		this.ts = ts;
		this.root = root;
	}

	public String E() {
		
		TokenKind tk = ts.tok.getKind();
		StringBuilder exp = new StringBuilder("");
		switch (tk) {
		
		case LITERAL_INT:
		{
			exp.append(ts.tok.getName());
			ts.next();
			if (ts.tok.getKind() == TokenKind.SEMICOLON)
				return exp.toString();
			else if (ts.tok.getKind() == TokenKind.PLUS_OP ||
					 ts.tok.getKind() == TokenKind.MINUS_OP ||
					 ts.tok.getKind() == TokenKind.MULTIPLY_OP ||
					 ts.tok.getKind() == TokenKind.DIVISION_OP) {
				exp.append(ts.tok.getName());
				ts.next();
				exp.append(E());
			}
			else {
				
				System.err.println("Unexpected expression to return. Exiting.");
				System.exit(255);
			}
		}
		break;
		
		case EXPRESSION:
			return ts.tok.getName();
		
		case OPEN_PARENTHESIS:
		{
			// exp.append(ts.tok.getName());
			ts.next();
			exp.append(E());
		}
		break;
		
		case CLOSE_PARENTHESIS:
		{
			exp.append(ts.tok.getName());
			ts.next();
		}
		break;
		
		default:
		{
			System.err.println("Bad token. Grammar error. Exiting. " + tk.toString());
			System.exit(255);
		}
		}
		
		return exp.toString();
	}
	
	public void S() {
		
		TokenKind tk = ts.tok.getKind();
		switch (tk){
		
		case RETURN:
		{
			Node<Keyword> node = new Node<Keyword>(new Keyword("ret", TokenKind.RETURN), NodeKind.RETURN);
			root.addChild(node);
			ts.next();
			Node<?> exp = Parser.createExpressionTree(E());
			node.addChild(exp);
		}
		break;
		
		default:
		{
			System.err.println("Bad token. Grammar error. Exiting.");
			System.exit(255);
		}
		}
		
		return;
	}

	public void parse() {

		if (ts.hasNext()) {
			
			ts.next();
			S();
		}
		
		if (ts.hasNext())
			System.out.println("Probaby an error.");
		return;
	}
}