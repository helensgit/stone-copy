package exception;

import java.io.IOException;

import common.token.Token;

public class ParseException extends Exception {

	public ParseException(Token t) {
		this("", t);
	}

    private static String location(Token t) {
        if (t == Token.EOF)
            return "the last line";
        else
            return "\"" + t.getText() + "\" at line " + t.getLineNumber();
    }
	
	public ParseException(String msg, Token t) {
		super("syntax error around " + location(t) + ". " + msg);
	}

	public ParseException(String msg) {
		super(msg);
	}

	public ParseException(IOException e) {
		super(e);
	}
}
