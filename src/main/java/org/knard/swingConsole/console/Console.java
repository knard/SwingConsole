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
import java.util.Arrays;

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

	private final static int FIRST_LINE_OFFSET = 1;

	private static final class ConsolePane extends Component {
		private Console console;

		public ConsolePane(Console console) {
			this.console = console;
		}

		@Override
		public Dimension getPreferredSize() {
			Dimension d = new Dimension(console.columnCount * console.getCharWidth(), console.rowCount * console.getCharHeight());
			return d;
		}

		@Override
		public void paint(Graphics g) {
			Rectangle clip = g.getClipBounds();
			g.setColor(Color.BLACK);
			g.fillRect(clip.x, clip.y, clip.width, clip.height);
			g.setColor(Color.GREEN);
			int startColumn = clip.x / console.getCharWidth();
			int endColumn = ((clip.x + clip.width) / console.getCharWidth()) + 1;
			if (endColumn >= console.columnCount) {
				endColumn = console.columnCount - 1;
			}
			int length = endColumn - startColumn;
			int startRow = clip.y / console.getCharHeight();
			int endRow = ((clip.y + clip.height) / console.getCharHeight()) + 1;
			if (endRow >= console.rowCount) {
				endRow = console.rowCount - 1;
			}
			g.setFont(console.getCharFont());
			for (int row = startRow; row <= endRow; row++) {
				g.drawChars(console.charBuffer[row], startColumn, length, 0, ((row + 1) * console.getCharHeight()) - console.getCharDescending() + FIRST_LINE_OFFSET);
			}
			if (console.cursorIsOn) {
				g.fillRect(console.x * console.getCharWidth(), console.y * console.getCharHeight() + FIRST_LINE_OFFSET, console.getCharWidth(), console.getCharHeight());
			}
		}

	}

	private Font FONT;
	private int CHAR_HEIGHT;
	private int CHAR_WIDTH;
	private int CHAR_DESCENT;

	private synchronized int getCharDescending() {
		if (CHAR_DESCENT == 0) {
			CHAR_DESCENT = getGraphics().getFontMetrics(getCharFont()).getMaxDescent();
		}
		return CHAR_DESCENT;
	}

	private synchronized Font getCharFont() {
		if (FONT == null) {
			FONT = Font.decode("Monospaced");
		}
		return FONT;
	}

	private synchronized int getCharHeight() {
		if (CHAR_HEIGHT == 0) {
			CHAR_HEIGHT = getGraphics().getFontMetrics(getCharFont()).getHeight();
		}
		return CHAR_HEIGHT;
	}

	private synchronized int getCharWidth() {
		if (CHAR_WIDTH == 0) {
			CHAR_WIDTH = getGraphics().getFontMetrics(getCharFont()).getWidths()['W'];
		}
		return CHAR_WIDTH;
	}

	private int columnCount;
	private int rowCount;
	private int x = 0, y = 0;
	private char[][] charBuffer;
	private ConsolePane consolePane;
	private boolean cursorIsOn;
	private static final byte[] lineSep = System.getProperty("line.separator").getBytes();

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
		for (int i = 0; i < rowCount; i++) {
			Arrays.fill(charBuffer[i], ' ');
		}
		this.setLayout(new BorderLayout());
		this.consolePane = new ConsolePane(this);
		JScrollPane scrollpane = new JScrollPane(consolePane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
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
		for (int i = 0; i < rowCount; i++) {
			Arrays.fill(charBuffer[i], ' ');
		}
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
	
	int endLineSequencePosition = 0;

	public void write(int c) {
		if( c == lineSep[endLineSequencePosition]) {
			endLineSequencePosition++;
			if( endLineSequencePosition == lineSep.length) {
				y++;
				x=0;
				endLineSequencePosition=0;
			}
			return;
		} else {
			endLineSequencePosition=0;
		}
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
		Arrays.fill(charBuffer[lastRow], ' ');
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
