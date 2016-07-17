package common.element;

import java.util.List;

import common.ast.ASTList;
import common.ast.ASTree;

public class NegativeExpr extends ASTList{

	public NegativeExpr(List <ASTree> children) {
		super(children);
	}
	
	public static ASTree createASTree(List <ASTree> list) {
		System.out.println("Create primary");
		return new NegativeExpr(list);
	}
	
	public ASTree operand() {
		return child(0);
	}
	
	@Override
	public String toString() {
		System.out.println("===================");
		return "-" + operand();
	}
}
