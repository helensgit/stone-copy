package lexer;


import org.junit.Test;

import ui.CodeDialog;

import common.token.Token;
import exception.ParseException;

public class Lexer_T {

	@Test
	public void test() throws ParseException {
		Lexer lexer = new Lexer(new CodeDialog());
		Token t = lexer.read();
		while(t != Token.EOF) {
			System.out.println("==>" + t.getText());
			t = lexer.read();
		}
		System.out.println("===");
	}

}
