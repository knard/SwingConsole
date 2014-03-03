package org.knard.swingConsole.console;

import java.io.IOException;

import jline.console.ConsoleReader;

public class SwingConsoleReader extends ConsoleReader {

	private Console console;

	public SwingConsoleReader(Console c) throws IOException {
		super(c.getInputStream(), c.getOutputStream(), c);
		this.console = c;
	}

	public Console getConsole() {
		return console;
	}

}
