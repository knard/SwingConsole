package org.knard.swingConsole.syntax;

import java.util.Collection;
import java.util.List;

import org.knard.swingConsole.helper.StringHelper.StringPart;

public interface NameHolder {

	Collection<String> getProposition(Syntax syntax, List<StringPart> parts, int index) throws ParseException;

}
