package eval;

import parser.BasicParser;
import parser.ClosureParser;
import parser.FuncParser;
import ui.CodeDialog;
import common.ast.ASTree;
import common.element.NullStatement;
import common.env.BasicEnv;
import common.env.NestedEnv;
import common.token.Token;
import javassist.gluonj.util.Loader;
import lexer.Lexer;
/*
 * 加上函数，闭包的测试
 * */
public class ClosureRunner {
	public static void main(String[] args) throws Throwable {
		Lexer l = new Lexer(new CodeDialog());
		ClosureParser cp = new ClosureParser();
		NestedEnv env = new NestedEnv();
		while (l.peek(0) != Token.EOF) {
			ASTree ast = cp.parse(l);
			if (!(ast instanceof NullStatement)) {
				System.out.println(ast);
				System.out.println("==>" + ast.eval(env));
			}
		}
	}
}
