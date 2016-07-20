package parser.eval;

import parser.BasicParser;
import ui.CodeDialog;
import common.ast.ASTree;
import common.element.NullStatement;
import common.elementEx.BasicEvaluator;
import common.env.BasicEnv;
import common.token.Token;
import javassist.gluonj.util.Loader;
import lexer.Lexer;

public class Runner {
	public static void main(String[] args) throws Throwable {
		Lexer l = new Lexer(new CodeDialog());
		BasicParser bp = new BasicParser();
		BasicEnv env = new BasicEnv();
		while (l.peek(0) != Token.EOF) {
			ASTree ast = bp.parse(l);
			if (!(ast instanceof NullStatement)) {
				System.out.println("==>" + ast.eval(env));
			}
		}
	}
}
