package parser;

import lexer.Lexer;
import ui.CodeDialog;

import common.ast.ASTree;
import common.token.Token;

import exception.ParseException;



public class ParserRunner {

	public static void main(String[] args) throws ParseException {
        Lexer l = new Lexer(new CodeDialog());
        BasicParser bp = new BasicParser();
        while (l.peek(0) != Token.EOF) {
            ASTree ast = bp.parse(l);
            System.out.println("=> " + ast.toString());
        }
        
    }
}
