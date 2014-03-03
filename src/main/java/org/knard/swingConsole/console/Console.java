package org.knard.swingConsole.console;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jline.Terminal;

public class Console extends JPanel implements Terminal {

	private static final class CursorThread extends Thread {

		private Console console;

		public CursorThread(Console console) {
			this.console = console;
		}

		@Override
		public void run() {
			while (true) {
				this.console.turnOnCursor();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					return;
				}
				this.console.turnOffCursor();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					return;
				}
			}
		}

	}

	private final static int CHARACTER_WIDTH = 7;
	private final static int CHARACTER_HEIGHT = 13;
	private final static int CHARACTER_BASELINE_OFFSET = -3;
	private final static int FIRST_LINE_OFFSET = 1;
	private final static Font FONT = Font.decode("Consolas 12");

	private static final class ConsolePane extends Component {
		private Console console;

		public ConsolePane(Console console) {
			this.console = console;
		}

		@Override
		public Dimension getPreferredSize() {
			Dimension d = new Dimension(console.columnCount * CHARACTER_WIDTH,
					console.rowCount * CHARACTER_HEIGHT);
			return d;
		}

		@Override
		public void paint(Graphics g) {
			Rectangle clip = g.getClipBounds();
			g.setColor(Color.BLACK);
			g.fillRect(clip.x, clip.y, clip.width, clip.height);
			g.setColor(Color.GREEN);
			int startColumn = clip.x / CHARACTER_WIDTH;
			int endColumn = ((clip.x + clip.width) / CHARACTER_WIDTH) + 1;
			if (endColumn >= console.columnCount) {
				endColumn = console.columnCount - 1;
			}
			int length = endColumn - startColumn;
			int startRow = clip.y / CHARACTER_HEIGHT;
			int endRow = ((clip.y + clip.height) / CHARACTER_HEIGHT) + 1;
			if (endRow >= console.rowCount) {
				endRow = console.rowCount - 1;
			}
			g.setFont(FONT);
			for (int row = startRow; row <= endRow; row++) {
				g.drawChars(console.charBuffer[row], startColumn, length, 0,
						((row + 1) * CHARACTER_HEIGHT)
								+ CHARACTER_BASELINE_OFFSET + FIRST_LINE_OFFSET);
			}
			if (console.cursorIsOn) {
				g.fillRect(console.x * CHARACTER_WIDTH, console.y
						* CHARACTER_HEIGHT + FIRST_LINE_OFFSET,
						CHARACTER_WIDTH, CHARACTER_HEIGHT);
			}
		}

	}

	private int columnCount;
	private int rowCount;
	private int x = 0, y = 0;
	private char[][] charBuffer;
	private ConsolePane consolePane;
	private boolean cursorIsOn;

	public Console() {
		this(80, 80);
	}

	private void turnOnCursor() {
		cursorIsOn = true;
		repaint();
	}

	private void turnOffCursor() {
		cursorIsOn = false;
		repaint();
	}

	private SwingConsoleOutputStream out;
	private SwingConsoleInputStream in;
	private Thread cursorThread;

	public OutputStream getOutputStream() {
		return this.out;
	}

	public Console(int width, int height) {
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		out = new SwingConsoleOutputStream(this);
		in = new SwingConsoleInputStream(this);
		this.columnCount = height;
		this.rowCount = width;
		this.charBuffer = new char[rowCount][columnCount];
		this.setLayout(new BorderLayout());
		this.consolePane = new ConsolePane(this);
		JScrollPane scrollpane = new JScrollPane(consolePane,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.add(scrollpane, BorderLayout.CENTER);

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int code = e.getKeyChar();
				if (code < 65535) {
					Console.this.in.addKey(code);
				}
			}
		});
	}

	public void init() throws Exception {
		// TODO Auto-generated method stub

	}

	public void restore() throws Exception {
		// TODO Auto-generated method stub

	}

	public void reset() throws Exception {
		this.charBuffer = new char[rowCount][columnCount];
		x = 0;
		y = 0;
		consolePane.repaint();
	}

	public boolean isSupported() {
		return true;
	}

	public boolean isAnsiSupported() {
		return true;
	}

	public OutputStream wrapOutIfNeeded(OutputStream out) {
		return getOutputStream();
	}

	public InputStream wrapInIfNeeded(InputStream in) throws IOException {
		return getInputStream();
	}

	public InputStream getInputStream() {
		return in;
	}

	public boolean hasWeirdWrap() {
		return false;
	}

	private boolean echoEnalbed;

	public boolean isEchoEnabled() {
		return echoEnalbed;
	}

	public void setEchoEnabled(boolean enabled) {
		echoEnalbed = enabled;
	}

	public void write(int c) {
		if (c == 10) {
			y++;
		} else if (c == 13) {
			x = 0;
		} else {
			charBuffer[y][x] = (char) c;
			x++;
		}
		if (x >= columnCount) {
			x = 0;
			y++;
		}
		if (y >= rowCount) {
			scroll();
			y--;
		}
		repaint();
	}

	private void scroll() {
		int lastRow = rowCount - 1;
		for (int i = 0; i < lastRow; i++) {
			charBuffer[i] = charBuffer[i + 1];
		}
		charBuffer[lastRow] = new char[columnCount];
	}

	public void displayCursor() {
		cursorThread = new CursorThread(this);
		cursorThread.start();
	}

	public void hideCursor() {
		if (cursorThread != null) {
			cursorThread.interrupt();
		}
		turnOffCursor();
		cursorThread = null;
	}

	public void cursorToColumn(int x) {
		this.x = x - 1;
		repaint();
	}

	public void clearChar() {
		charBuffer[y][x] = ' ';
	}

	public int getColumnCount() {
		return columnCount;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void cursorUpBeginningPreviousLine() {
		this.y--;
		this.x = 0;
		repaint();
	}
}
