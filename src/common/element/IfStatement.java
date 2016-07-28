package common.element;

import java.util.List;

import common.ast.ASTList;
import common.ast.ASTree;
import common.env.Environment;
import exception.StoneException;

public class IfStatement extends ASTList {

	public IfStatement(List <ASTree> children) {
		super(children);
	}

	protected ASTree condition() {
		return child(0);
	}
	
	protected ASTree body() {
		return child(1);
	}
	
	protected ASTree elseBody() {
		if(children.size()<=2)
			return null;
		else
			return child(2);
	}
	
	@Override
	public String toString() {
		String ret = "";
		ret = "if:(if(" + condition() +") " + body() + 
				" else " + elseBody() + ")"; 
		return ret;
	}
	
	public static ASTree createASTree(List<ASTree> list) {
		return new IfStatement(list);
	}

	@Override
	public Object eval(Environment env) {
		Object condition = condition().eval(env);
		if(condition instanceof Integer && (Integer)condition == BinaryExpr.TRUE)
			return body().eval(env);
		else if(elseBody() != null)
			return elseBody().eval(env);
		else
			return 0; //随便返回一个东西
	}
	
	
}
