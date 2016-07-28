package common.element;

import java.util.List;

import common.ast.ASTList;
import common.ast.ASTree;
import common.env.Environment;
import exception.StoneException;

public class NegativeExpr extends ASTList{

	public NegativeExpr(List <ASTree> children) {
		super(children);
	}
	
	public static ASTree createASTree(List <ASTree> list) {
		return new NegativeExpr(list);
	}
	
	public ASTree operand() {
		return child(0);
	}
	
	@Override
	public String toString() {
		return "-" + operand();
	}
	
	@Override
	public Object eval(Environment env) {
		Object ret = operand().eval(env);
		if(!(ret instanceof Integer)) 
			throw new StoneException("bad type for - ", this);
		else
			return -1 * (Integer)ret;
	}
}
