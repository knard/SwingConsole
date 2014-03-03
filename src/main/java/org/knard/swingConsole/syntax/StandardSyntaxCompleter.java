package org.knard.swingConsole.syntax;

import java.util.List;

import jline.console.completer.Completer;

public class StandardSyntaxCompleter implements Completer {

	private Syntax syntax;

	public StandardSyntaxCompleter(Syntax syntax) {
		this.syntax = syntax;
	}

	@Override
	public int complete(String buffer, int cursorPos, List<CharSequence> propositions) {
		try {
			return syntax.getPropositions(buffer, cursorPos, propositions);
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}
	}

}
