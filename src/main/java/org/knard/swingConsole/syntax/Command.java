package org.knard.swingConsole.syntax;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Command implements Iterable<String> {

	private String name;
	private List<String> arguments = new ArrayList<String>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Iterator<String> iterator() {
		return arguments.iterator();
	}
	
	public String getArgument(int index) {
		return arguments.get(index);
	}
	
	public int getArgumentCount() {
		return arguments.size();
	}
	
	public void addArgument(String argument) {
		arguments.add(argument);
	}
	
	
}
