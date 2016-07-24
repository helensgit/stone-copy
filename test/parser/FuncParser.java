package parser;

import static parser.Parser.rule; 
import common.ast.Name;
import common.element.Arguments;
import common.element.DefStmnt;
import common.element.NativeArg;
import common.element.NullStatement;
import common.element.ParameterList;

public class FuncParser extends BasicParser {
    Parser param = rule().identifier(Name.class, reserved);
    Parser params = rule(ParameterList.class)
                        .ast(param).repeat(rule().sep(",").ast(param));
    Parser paramList = rule().sep("(").maybe(params).sep(")");
    Parser def = rule(DefStmnt.class)
                     .sep("def").identifier(Name.class, reserved).ast(paramList).ast(block);
//    Parser args = rule(Arguments.class)
//                      .ast(expr).repeat(rule().sep(",").ast(expr));
    Parser args = rule(NativeArg.class)
                      .ast(expr).repeat(rule().sep(",").ast(expr));
    Parser postfix = rule().sep("(").maybe(args).sep(")");
    
    public FuncParser() {
        reserved.add(")");
        primary.repeat(postfix);
//        simple.option(args);
        program.insertChoice(def);
    }
    
}
