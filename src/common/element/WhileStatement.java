package common.element;

import java.util.List;

import common.ast.ASTList;
import common.ast.ASTree;
import common.env.Environment;

public class WhileStatement extends ASTList {

	public WhileStatement(List <ASTree> children) {
		super(children);
	}

	protected ASTree condition() {
		return child(0);
	}
	
	protected ASTree body() {
		return child(1);
	}
	
	@Override
	public String toString() {
		String ret = "";
		ret = "whileStament: (while(" + condition() +") " + body()+")";	
		return ret;
	}
	
	public static ASTree createASTree(List<ASTree> list) {
		return new WhileStatement(list);
	}

	@Override
	public Object eval(Environment env) {
		Object ret = null;
		while(true) {
			Object condition = condition().eval(env);
			if((condition instanceof Integer) && (Integer)condition == BinaryExpr.FALSE) {
				break;
			}
			ret = body().eval(env);
		}
		return ret;
	}

}
