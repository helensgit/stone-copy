package common.element;

import java.util.ArrayList;
import java.util.List;

import common.ast.ASTList;
import common.ast.ASTree;

public class NullStatement extends ASTList{

	public NullStatement(List <ASTree> children) {
		super(children);
	}

	public static ASTree createASTree(List <ASTree> list) {
		return new NullStatement(list);
	}
	
	@Override
	public String toString() {
		return "nullStament";
	}
	
}
