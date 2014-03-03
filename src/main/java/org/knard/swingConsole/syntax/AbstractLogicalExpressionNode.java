package org.knard.swingConsole.syntax;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLogicalExpressionNode implements LogicalExpressionNode {

	protected List<SyntaxNode> childrenNodes = new ArrayList<SyntaxNode>();

	@Override
	public void add(SyntaxNode e) {
		if (e instanceof LogicalExpressionNode) {
			LogicalExpressionNode n = (LogicalExpressionNode) e;
			if (n.hasOnlyOneChild()) {
				e = n.getUniqueChild();
			}
		}
		childrenNodes.add(e);
	}

	@Override
	public boolean hasOnlyOneChild() {
		return childrenNodes.size() == 1;
	}

	@Override
	public SyntaxNode getUniqueChild() {
		return childrenNodes.get(0);
	}

}
