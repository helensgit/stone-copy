package common.element;

import java.util.List;

import common.ast.ASTList;
import common.ast.ASTree;
import common.env.Environment;

public class Fun extends ASTList {

	public Fun(List<ASTree> children) {
		super(children);
	}

	public ParameterList parameters() {
		return (ParameterList) child(0);
	}

	public static ASTree createASTree(List<ASTree> list) {
		return new Fun(list);
	}

	public BlockStament body() {
		return (BlockStament)child(1);
	}

	@Override
	public Object eval(Environment env) {
		return new Function(parameters(), body(), env);
	}

	@Override
	public String toString() {
		return "(fun " + parameters() + " " + body() + ")";
	}
}
