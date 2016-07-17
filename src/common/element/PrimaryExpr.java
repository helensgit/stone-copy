package common.element;

import java.util.List;

import common.ast.ASTList;
import common.ast.ASTree;

public class PrimaryExpr extends ASTList{

	public PrimaryExpr(List <ASTree> children) {
		super(children);
	}
	
	public static ASTree createASTree(List<ASTree> list) {
		if(list.size() == 1)
			return list.get(0);
		else
			return new PrimaryExpr(list);
	}
}
