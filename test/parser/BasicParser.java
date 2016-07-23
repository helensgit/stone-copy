package parser;


import static parser.Parser.rule;

import java.util.HashSet;

import lexer.Lexer;
import parser.Parser.Operators;
import common.ast.ASTree;
import common.ast.Name;
import common.ast.NumberLiteral;
import common.ast.StringLiteral;
import common.element.BlockStament;
import common.element.IfStatement;
import common.element.NegativeExpr;
import common.element.NullStatement;
import common.element.PrimaryExpr;
import common.element.WhileStatement;
import common.token.Token;
import exception.ParseException;


public class BasicParser {
    HashSet<String> reserved = new HashSet<String>();
    Operators operators = new Operators();
    Parser expr0 = rule();
    Parser primary = rule(PrimaryExpr.class)
        .or(rule().sep("(").ast(expr0).sep(")"),
            rule().number(NumberLiteral.class),
            rule().identifier(Name.class, reserved),
            rule().string(StringLiteral.class)).descp("primary");
    Parser factor = rule().or(rule(NegativeExpr.class).sep("-").ast(primary),
                              primary);                            
    
    Parser expr = expr0.expression(factor, operators);

    Parser statement0 = rule();
    Parser block = rule(BlockStament.class)
        .sep("{").option(statement0)
        .repeat(rule().sep(";", Token.EOL).option(statement0))
        .sep("}");
    
    Parser simple = rule(PrimaryExpr.class).ast(expr);
    Parser statement = statement0.or(
            rule(IfStatement.class).sep("if").ast(expr).ast(block)
                               .option(rule().sep("else").ast(block)),
            rule(WhileStatement.class).sep("while").ast(expr).ast(block),
            simple);

    Parser program = rule().or(statement, rule(NullStatement.class))
                           .sep(";", Token.EOL).descp("program");

    public BasicParser() {
        reserved.add(";");
        reserved.add("}");
        reserved.add("{");
        reserved.add(")");
        reserved.add("(");
        
        reserved.add(Token.EOL);

        operators.add("=", 1, Operators.RIGHT);
        
        operators.add("==", 2, Operators.LEFT);
        operators.add(">", 2, Operators.LEFT);
        operators.add("<", 2, Operators.LEFT);
        
        operators.add("+", 3, Operators.LEFT);
        operators.add("-", 3, Operators.LEFT);
        
        operators.add("*", 4, Operators.LEFT);
        operators.add("/", 4, Operators.LEFT);
        operators.add("%", 4, Operators.LEFT);
    }
    public ASTree parse(Lexer lexer) throws ParseException {
        return program.parse(lexer);
    }
}
