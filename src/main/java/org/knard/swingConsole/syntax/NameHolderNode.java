package org.knard.swingConsole.syntax;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.knard.swingConsole.helper.StringHelper.StringPart;

public class NameHolderNode implements SyntaxNode {

	private String name;

	public NameHolderNode(String name) {
		this.name = name;
	}

	@Override
	public List<String> getPropositions(Syntax syntax, List<StringPart> parts, int index, String filter) throws ParseException {
		NameHolder holder = syntax.getNameHolder(name);
		Collection<String> values = holder.getProposition(syntax, parts, index);
		List<String> results = new ArrayList<String>();
		for (String value : values) {
			if( value.startsWith(filter)) {
				results.add(filter);
			}
		}
		return results;
	}

	@Override
	public void setNodeSyntaxicPosition(int pos) {
	}

}
