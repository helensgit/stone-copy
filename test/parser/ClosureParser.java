package parser;

import static parser.Parser.rule; 
import common.ast.Name;
import common.element.Arguments;
import common.element.DefStmnt;
import common.element.Fun;
import common.element.ParameterList;

public class ClosureParser extends FuncParser {
    
    public ClosureParser() {
    	primary.insertChoice(rule(Fun.class)
    						.sep("fun").ast(paramList).ast(block));
    }
    
}
