package common.token;

import exception.StoneException;

public abstract class Token {

	public static String EOL = "\\n";
	public static Token EOF = new Token(-1) { };
	
	private int lineNumber;
	public Token(int lineNo) {
		lineNumber = lineNo;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}

	public boolean isString() {
		return false;
	}
	
	public boolean isNumber() {
		return false;
	}
	
	public boolean isIdentifier() {
		return false;
	}
	
	public String getText() {
		return "";
	}
	
	public int getNumber() {
		throw new StoneException("this is no number");
	}
	
}
