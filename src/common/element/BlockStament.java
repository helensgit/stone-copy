package common.element;

import java.util.List;

import common.ast.ASTList;
import common.ast.ASTree;

public class BlockStament extends ASTList{

	public BlockStament(List<ASTree> children) {
		super(children);
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
	
	public static ASTree createASTree(List<ASTree> list) {
		return new BlockStament(list);
	}
}
