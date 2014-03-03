package org.knard.swingConsole.console;

import java.io.IOException;
import java.io.InputStream;

public class SwingConsoleInputStream extends InputStream {

	volatile int val;
	private Console console;

	public SwingConsoleInputStream(Console console) {
		this.console = console;
	}

	@Override
	public int read() throws IOException {
		console.displayCursor();
		synchronized (this) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				return -1;
			}
		}
		console.hideCursor();
		return val;
	}

	public void addKey(int code) {
		val = code;
		synchronized (this) {
			this.notifyAll();
		}
	}

}
