package common.element;

import java.util.List;

import common.ast.ASTList;
import common.ast.ASTree;

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
		ret = "(if(" + condition() +") " + body() + 
				" else " + elseBody() + ")"; 
		return ret;
	}
	
	public static ASTree createASTree(List<ASTree> list) {
		return new IfStatement(list);
	}


}
