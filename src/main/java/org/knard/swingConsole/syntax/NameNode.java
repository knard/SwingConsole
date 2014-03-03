package org.knard.swingConsole.syntax;

import java.util.ArrayList;
import java.util.List;

import org.knard.swingConsole.helper.StringHelper.StringPart;

public class NameNode extends AbstractSyntaxNode implements SyntaxNode {

	private String name;

	public NameNode(String name) {
		this.name = name;
	}

	@Override
	public List<String> getPropositions(Syntax s, List<StringPart> parts, int index, String filter) {
		if( index != pos) {
			return new ArrayList<String>(0);
		}
		List<String> propositions = new ArrayList<String>(1);
		if( name.startsWith(filter) ) {
			propositions.add(name);
		}
		return propositions;
	}

}
