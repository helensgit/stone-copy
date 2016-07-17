package ui;

import java.io.IOException;
import java.io.Reader;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class CodeDialog extends Reader {
	private String buffer;
	private int pos;

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		if (buffer == null) {
			String in = showDialog();
			if (in == null)
				return -1;
			else {
				System.out.println(in);
				buffer = in + "\n";
				pos = 0;
			}
		}
		int size = 0;
		int length = buffer.length();
		while (pos < length && size < len) {
			cbuf[off + size] = buffer.charAt(pos);
			size++;
			pos++;
		}
		if (pos == length)
			buffer = null;
		return size;
	}

	private String showDialog() {
		JTextArea textArea = new JTextArea(20, 40);
		JScrollPane jScrollPane = new JScrollPane(textArea);
		int result = JOptionPane.showOptionDialog(null, jScrollPane, "Input",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
				null, null);

		if (result == JOptionPane.OK_OPTION)
			return textArea.getText();
		else
			return null;
	}

	@Override
	public void close() throws IOException {
	}

}
