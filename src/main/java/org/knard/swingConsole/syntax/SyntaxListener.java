package org.knard.swingConsole.syntax;

import java.util.Stack;

import org.knard.swingConsole.SyntaxParserBaseListener;
import org.knard.swingConsole.SyntaxParserListener;
import org.knard.swingConsole.SyntaxParserParser.Followed_expressionContext;
import org.knard.swingConsole.SyntaxParserParser.NameContext;
import org.knard.swingConsole.SyntaxParserParser.Name_holderContext;
import org.knard.swingConsole.SyntaxParserParser.Or_expressionContext;
import org.knard.swingConsole.SyntaxParserParser.SyntaxContext;

public class SyntaxListener extends SyntaxParserBaseListener implements SyntaxParserListener {

	Stack<SyntaxNode> stack = new Stack<SyntaxNode>();
	private Syntax syntax;

	@Override
	public void enterSyntax(SyntaxContext ctx) {
		stack.push(new Syntax());
	}

	@Override
	public void exitSyntax(SyntaxContext ctx) {
		syntax = (Syntax) stack.pop();
		syntax.setNodeSyntaxicPosition(0);
	}

	@Override
	public void enterOr_expression(Or_expressionContext ctx) {
		stack.push(new OrExpression());
	}

	@Override
	public void exitOr_expression(Or_expressionContext ctx) {
		OrExpression e = (OrExpression) stack.pop();
		((LogicalExpressionNode) stack.peek()).add(e);
	}

	@Override
	public void enterFollowed_expression(Followed_expressionContext ctx) {
		stack.push(new FollowedExpression());
	}

	@Override
	public void exitFollowed_expression(Followed_expressionContext ctx) {
		FollowedExpression e = (FollowedExpression) stack.pop();
		((LogicalExpressionNode) stack.peek()).add(e);
	}

	@Override
	public void enterName(NameContext ctx) {
		stack.push(new NameNode(ctx.getChild(1).getText()));
	}

	@Override
	public void exitName(NameContext ctx) {
		NameNode e = (NameNode) stack.pop();
		((LogicalExpressionNode) stack.peek()).add(e);
	}

	@Override
	public void enterName_holder(Name_holderContext ctx) {
		stack.push(new NameHolderNode(ctx.getText()));
	}

	@Override
	public void exitName_holder(Name_holderContext ctx) {
		NameHolderNode e = (NameHolderNode) stack.pop();
		((LogicalExpressionNode) stack.peek()).add(e);
	}

	public Syntax getSyntax() {
		return syntax;
	}

}
