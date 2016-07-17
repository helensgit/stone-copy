package common.element;

import java.util.List;

import common.ast.ASTLeaf;
import common.ast.ASTList;
import common.ast.ASTree;

public class BinaryExpr extends ASTList{

	public BinaryExpr(List<ASTree> children) {
		super(children);
	}

	public ASTree left() {
		return child(0);
	}
	
	public String operator() {
		return ((ASTLeaf)child(1)).getToken().getText();
	}
	
	public ASTree right() {
		return child(2);
	}
	
	public static ASTree createASTree(List<ASTree> list) {
		return new BinaryExpr(list);
	}
	
	@Override
	public String toString() {
		return super.toString();
	}

}
