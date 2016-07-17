package parser.eval;

import lexer.Lexer;
import parser.BasicParser;
import ui.CodeDialog;
import common.ast.ASTree;
import common.element.NullStatement;
import common.elementEx.BasicEvaluator;
import common.env.BasicEnv;
import common.env.Environment;
import common.token.Token;
import exception.ParseException;

public class BasicInterpreter {
    public static void main(String[] args) throws ParseException {
        run(new BasicParser(), new BasicEnv());
    }
    public static void run(BasicParser bp, Environment env)
        throws ParseException
    {
        Lexer lexer = new Lexer(new CodeDialog());
        while (lexer.peek(0) != Token.EOF) {
            ASTree t = bp.parse(lexer);
            if (!(t instanceof NullStatement)) {
                Object r = ((BasicEvaluator.ASTreeEx)t).eval(env);
                System.out.println("=> " + r);
            }
        }
    }
}
