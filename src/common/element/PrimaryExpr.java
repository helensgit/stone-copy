package common.element;

import java.util.List;

import common.ast.ASTList;
import common.ast.ASTree;
import common.env.Environment;

public class PrimaryExpr extends ASTList{

	public static ASTree createASTree(List<ASTree> list) {
		if(list.size() == 1) 
			return list.get(0);
		else
			return new PrimaryExpr(list);
	}

	public PrimaryExpr(List <ASTree> children) {
		super(children);
	}
	
	public ASTree operand() {
		return child(0);
	}
	public boolean hasPostfix(int nest) {
		return numOfChildren() - nest > 1;
	}
	
	public Postfix posfix(int nest) {
		return (Postfix) child(numOfChildren() - nest - 1);
	}
	
	@Override
	public Object eval(Environment env) {
		return evalSubExpr(env, 0);
	}
	
	private Object evalSubExpr(Environment env, int nest) {
		if(hasPostfix(nest)) {
			Object target = evalSubExpr(env, nest+1);
			return ((Postfix)posfix(nest)).eval(env, target);
		}
		else 
			return operand().eval(env);
	}

	@Override
	public String toString() {
		return "primary:" + super.toString();
	}
}
