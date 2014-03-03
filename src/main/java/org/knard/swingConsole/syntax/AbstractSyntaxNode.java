package org.knard.swingConsole.syntax;


public abstract class AbstractSyntaxNode implements SyntaxNode {

	protected int pos;

	@Override
	public void setNodeSyntaxicPosition(int pos) {
		this.pos = pos;
	}

}
