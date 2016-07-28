package common.element;

import java.util.List;

import common.ast.ASTList;
import common.ast.ASTree;
import common.env.Environment;

public class BlockStament extends ASTList{

	public BlockStament(List<ASTree> children) {
		super(children);
	}
	
	@Override
	public String toString() {
		return "block:" + super.toString();
	}
	
	public static ASTree createASTree(List<ASTree> list) {
		return new BlockStament(list);
	}
	
	@Override
	public Object eval(Environment env) {
		Object ret = null;
		for(ASTree t : this) {
			if(!(t instanceof NullStatement))
			ret = t.eval(env);
		}
		return ret;
	}
}
