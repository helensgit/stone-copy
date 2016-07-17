package lexer;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.token.Token;

import exception.ParseException;
import exception.StoneException;

@SuppressWarnings(value = "all")
public class Lexer {
	public static final String regexPat = "\\s*((//.*)|([0-9]+)|(\"(\\\\\"|\\\\\\\\|\\\\n|[^\"])*\")"
			+ "|[A-Z_a-z][A-Z_a-z0-9]*|==|<=|>=|&&|\\|\\||\\p{Punct})?";
	private Pattern pattern = Pattern.compile(regexPat);
	private List<Token> queue = new ArrayList<Token>();
	private LineNumberReader reader;
	private boolean hasMore;

	public Lexer(Reader r) {
		this.reader = new LineNumberReader(r);
		hasMore = true;
	}

	public Token read() throws ParseException {
		if (fillQueue(0))
			return queue.remove(0);
		else
			return Token.EOF;
	}

	private boolean fillQueue(int i) throws ParseException {
		while (i >= queue.size()) {
			if (hasMore) {
//				System.out.println("等待");
				readLine();
//				System.out.println("等待结束");
			}
			else
				return false;
		}
		return true;
	}

	private void readLine() throws ParseException {
		String line = null;
		try {
			line = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (line == null) {
			hasMore = false;
			return;
		}
		int lineNo = reader.getLineNumber();
		Matcher matcher = pattern.matcher(line);
		matcher.useTransparentBounds(true).useAnchoringBounds(false);
		int pos, endPos;
		pos = 0;
		endPos = line.length();
		while (pos < endPos) {
			matcher.region(pos, endPos);
			if (matcher.lookingAt()) {
				addToken(lineNo, matcher);
				pos = matcher.end();
			} else
				throw new ParseException("bad token at line " + lineNo);
		}
		queue.add(new IdToken(lineNo, Token.EOL));
	}

	private void addToken(int lineNo, Matcher matcher) {
		String m = matcher.group(1);
		// m 不是空格
		if (m != null) {
			// 非注释
			if (matcher.group(2) == null) {
				Token token;
				if (matcher.group(3) != null)
					token = new NumToken(lineNo, Integer.parseInt(m));
				else if (matcher.group(4) != null)
					token = new StrToken(lineNo, toStringLiteral(m, lineNo));
				else
					token = new IdToken(lineNo, m);
				queue.add(token);
			}
		}

	}

	private String toStringLiteral(String m, int lineNo) {
		StringBuilder sb = new StringBuilder();
		int len = m.length() - 1;
		char c;
		for (int i = 1; i < len; i++) {
			if (m.charAt(i) == '\\') {
				if (i + 1 < len) {
					switch (m.charAt(i + 1)) {
					case 'n':
						c = '\n';
						break;
					case '\\':
						c = '\\';
						break;
					case '"':
						c = '"';
						break;
					default:
						throw new StoneException("bad string token " + m + " at line "
								+ lineNo);
					}
					i++;
				} else {
					throw new StoneException("bad string token" + m + " at line "
							+ lineNo);
				}
			} else
				c = m.charAt(i);
			sb.append(c);
		}
		return sb.toString();
	}
//
//	private static String[] sep = new String[]{"\\n"};
//	private void skip() {
//		boolean flag = false;
//		while(queue.size() > 0 && !flag) {
//			flag = true;
//			Token t = queue.get(0);
//			for(String s : sep) {
//				if(s.equals(t.getText())) {
//					queue.remove(0);
//					flag = false;
//					break;
//				}
//			}
//		}
//	}

	public Token peek(int index) throws ParseException {
		if (fillQueue(index)) {
			return queue.get(index);
		}
		else
			return Token.EOF;
	}

	protected static class StrToken extends Token {
		private String content;

		public StrToken(int lineNo, String content) {
			super(lineNo);
			this.content = content;
		}

		@Override
		public boolean isString() {
			return true;
		}

		@Override
		public String getText() {
			return content;
		}
	}

	protected static class NumToken extends Token {
		int value;

		public NumToken(int lineNo, int value) {
			super(lineNo);
			this.value = value;
		}

		@Override
		public boolean isNumber() {
			return true;
		}

		@Override
		public int getNumber() {
			return value;
		}

		@Override
		public String getText() {
			return "" + value;
		}
	}

	protected static class IdToken extends Token {
		String name;

		public IdToken(int lineNo, String name) {
			super(lineNo);
			this.name = name;
		}

		@Override
		public boolean isIdentifier() {
			return true;
		}

		@Override
		public String getText() {
			return name;
		}
	}
}
