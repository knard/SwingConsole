package org.knard.swingConsole.syntax;

import java.util.List;

import org.knard.swingConsole.helper.StringHelper.StringPart;

public class FollowedExpression extends AbstractLogicalExpressionNode implements LogicalExpressionNode {

	@Override
	public List<String> getPropositions(Syntax syntax, List<StringPart> parts, int index, String filter) throws ParseException {
		int childIndex = index - pos;
		if (childIndex >= childrenNodes.size()) {
			childIndex = childrenNodes.size() - 1;
		}
		return childrenNodes.get(childIndex).getPropositions(syntax, parts, index, filter);
	}

	@Override
	public void setNodeSyntaxicPosition(int pos) {
		super.setNodeSyntaxicPosition(pos);
		for (SyntaxNode node : childrenNodes) {
			node.setNodeSyntaxicPosition(pos++);
		}
	}

}
