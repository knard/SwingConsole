package org.knard.swingConsole.syntax;

import java.util.List;

import org.knard.swingConsole.helper.StringHelper.StringPart;

public interface SyntaxNode {

	List<String> getPropositions(Syntax syntax, List<StringPart> parts, int index, String filter) throws ParseException;

	void setNodeSyntaxicPosition(int pos);

}
