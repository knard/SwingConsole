package org.knard.swingConsole.syntax;

import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.knard.swingConsole.SyntaxParserLexer;
import org.knard.swingConsole.SyntaxParserParser;
import org.knard.swingConsole.SyntaxParserParser.SyntaxContext;
import org.knard.swingConsole.helper.StringHelper;
import org.knard.swingConsole.helper.StringHelper.StringPart;

public class Syntax extends AbstractLogicalExpressionNode implements LogicalExpressionNode {

	private Map<String, NameHolder> holders;

	public static Syntax createSyntax(String exp, Map<String, NameHolder> holders) {
		ANTLRInputStream inputStream = new ANTLRInputStream(exp);
		SyntaxParserLexer lexer = new SyntaxParserLexer(inputStream);
		BufferedTokenStream tokenStream = new BufferedTokenStream(lexer);
		SyntaxParserParser parser = new SyntaxParserParser(tokenStream);
		SyntaxContext ctx = parser.syntax();
		ParseTreeWalker walker = new ParseTreeWalker();
		SyntaxListener l = new SyntaxListener();
		walker.walk(l, ctx);
		Syntax s = l.getSyntax();
		s.holders = holders;
		return s;
	}

	public int getPropositions(String buffer, int position, List<CharSequence> propositions) throws ParseException {
		List<StringPart> parts = StringHelper.split(buffer);
		if (parts.size() == 0) {
			parts.add(new StringPart("", 0, 1));
		}
		StringPart part = null;
		for (int i = 0; i < parts.size(); i++) {
			part = parts.get(i);
			if (position <= part.end) {
				if (position <= part.start) {
					part = new StringPart("", position, position);
					parts.add(i, part);
				}
				int length = position - part.start;
				String filter = null;
				if (part.part.length() == length) {
					filter = part.part;
				} else {
					filter = part.part.substring(0, length);
				}
				List<String> props = getUniqueChild().getPropositions(this, parts, i, filter);
				if (props.size() == 0) {
					return -1;
				}
				propositions.addAll(props);
				return part.start;
			}
		}
		part = new StringPart("", position, position);
		parts.add(part);
		List<String> props = getUniqueChild().getPropositions(this, parts, parts.size()-1, "");
		if (props.size() == 0) {
			return -1;
		}
		propositions.addAll(props);
		return part.start;
	}

	public NameHolder getNameHolder(String name) {
		return holders.get(name);
	}

	@Override
	public List<String> getPropositions(Syntax syntax, List<StringPart> parts, int index, String filter) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setNodeSyntaxicPosition(int pos) {
		getUniqueChild().setNodeSyntaxicPosition(0);
	}

}
