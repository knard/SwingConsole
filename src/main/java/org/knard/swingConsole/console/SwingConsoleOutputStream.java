package org.knard.swingConsole.console;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.fusesource.jansi.AnsiOutputStream;

public class SwingConsoleOutputStream extends AnsiOutputStream {

	private static final class OutputInterceptorStream extends OutputStream {

		private Console console;

		public OutputInterceptorStream(Console c) {
			this.console = c;
		}

		@Override
		public void write(byte[] b) throws IOException {
			synchronized (console) {
				super.write(b);
			}
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			synchronized (console) {
				super.write(b, off, len);
			}
		}

		@Override
		public void write(int c) throws IOException {
			synchronized (console) {
				console.write(c);
			}
		}

	}

	private Console console;

	@Override
	protected void processAttributeRest() throws IOException {
		System.out.println("processAttributeRest");
		super.processAttributeRest();
	}

	@Override
	protected void processChangeIconName(String label) {
		System.out.println("processChangeIconName");
		super.processChangeIconName(label);
	}

	@Override
	protected void processChangeIconNameAndWindowTitle(String label) {
		System.out.println("processChangeIconNameAndWindowTitle");
		super.processChangeIconNameAndWindowTitle(label);
	}

	@Override
	protected void processChangeWindowTitle(String label) {
		System.out.println("processChangeWindowTitle");
		super.processChangeWindowTitle(label);
	}

	@Override
	protected void processCursorDown(int count) throws IOException {
		System.out.println("processCursorDown");
		super.processCursorDown(count);
	}

	@Override
	protected void processCursorDownLine(int arg0) throws IOException {
		System.out.println("processCursorDownLine");
		super.processCursorDownLine(arg0);
	}

	@Override
	protected void processCursorLeft(int count) throws IOException {
		System.out.println("processCursorLeft");
		super.processCursorLeft(count);
	}

	@Override
	protected void processCursorRight(int arg0) throws IOException {
		System.out.println("processCursorRight");
		super.processCursorRight(arg0);
	}

	@Override
	protected void processCursorTo(int row, int col) throws IOException {
		System.out.println("processCursorTo");
		super.processCursorTo(row, col);
	}

	@Override
	protected void processCursorToColumn(int x) throws IOException {
		console.cursorToColumn(x);
	}

	@Override
	protected void processCursorUp(int count) throws IOException {
		System.out.println("processCursorUp");
		super.processCursorUp(count);
	}

	@Override
	protected void processCursorUpLine(int count) throws IOException {
		console.cursorUpBeginningPreviousLine();
	}

	@Override
	protected void processDefaultBackgroundColor() throws IOException {
		System.out.println("processDefaultBackgroundColor");
		super.processDefaultBackgroundColor();
	}

	@Override
	protected void processDefaultTextColor() throws IOException {
		System.out.println("processDefaultTextColor");
		super.processDefaultTextColor();
	}

	@Override
	protected void processEraseLine(int eraseOption) throws IOException {
		console.clearChar();
	}

	@Override
	protected void processEraseScreen(int eraseOption) throws IOException {
		System.out.println("processEraseScreen");
		super.processEraseScreen(eraseOption);
	}

	@Override
	protected void processRestoreCursorPosition() throws IOException {
		System.out.println("processRestoreCursorPosition");
		super.processRestoreCursorPosition();
	}

	@Override
	protected void processSaveCursorPosition() throws IOException {
		System.out.println("processSaveCursorPosition");
		super.processSaveCursorPosition();
	}

	@Override
	protected void processScrollDown(int optionInt) throws IOException {
		System.out.println("processScrollDown");
		super.processScrollDown(optionInt);
	}

	@Override
	protected void processScrollUp(int optionInt) throws IOException {
		System.out.println("processScrollUp");
		super.processScrollUp(optionInt);
	}

	@Override
	protected void processSetAttribute(int attribute) throws IOException {
		System.out.println("processSetAttribute");
		super.processSetAttribute(attribute);
	}

	@Override
	protected void processSetBackgroundColor(int color) throws IOException {
		System.out.println("processSetBackgroundColor");
		super.processSetBackgroundColor(color);
	}

	@Override
	protected void processSetForegroundColor(int color) throws IOException {
		System.out.println("processSetForegroundColor");
		super.processSetForegroundColor(color);
	}

	@Override
	protected void processUnknownExtension(ArrayList<Object> options,
			int command) {
		System.out.println("processUnknownExtension");
		super.processUnknownExtension(options, command);
	}

	@Override
	protected void processUnknownOperatingSystemCommand(int command,
			String param) {
		System.out.println("processUnknownOperatingSystemCommand");
		super.processUnknownOperatingSystemCommand(command, param);
	}

	public SwingConsoleOutputStream(Console c) {
		super(new OutputInterceptorStream(c));
		this.console = c;
	}

}
