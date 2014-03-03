package org.knard.swingConsole.syntax;

public interface LogicalExpressionNode extends SyntaxNode {

	void add(SyntaxNode e);

	boolean hasOnlyOneChild();

	SyntaxNode getUniqueChild();

}
