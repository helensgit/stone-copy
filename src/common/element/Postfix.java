package common.element;

import java.util.List;

import common.ast.ASTList;
import common.ast.ASTree;
import common.env.Environment;

public abstract class Postfix extends ASTList {

	public Postfix(List<ASTree> children) {
		super(children);
	}
	
	public abstract Object eval(Environment env, Object value);
}
