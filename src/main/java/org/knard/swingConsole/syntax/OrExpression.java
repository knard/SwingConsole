package org.knard.swingConsole.syntax;

import java.util.ArrayList;
import java.util.List;

import org.knard.swingConsole.helper.StringHelper.StringPart;

public class OrExpression extends AbstractLogicalExpressionNode implements LogicalExpressionNode {

	@Override
	public List<String> getPropositions(Syntax syntax, List<StringPart> parts, int index, String filter) throws ParseException {
		List<String> results = new ArrayList<String>();
		for (SyntaxNode node : childrenNodes) {
			//select only branches of the or that we should explore
			if( index == pos || node.getPropositions(syntax, parts, pos, parts.get(pos).part).size()>0) { //should not use the getProposition method, I should create a validation method
				results.addAll(node.getPropositions(syntax, parts, index, filter));
			}
		}
		return results;
	}

	@Override
	public void setNodeSyntaxicPosition(int pos) {
		super.setNodeSyntaxicPosition(pos);
		for (SyntaxNode node : childrenNodes) {
			node.setNodeSyntaxicPosition(pos);
		}
	}

}
