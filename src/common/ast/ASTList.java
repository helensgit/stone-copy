package common.ast;

import java.util.Iterator;
import java.util.List;

import common.element.PrimaryExpr;

public class ASTList extends ASTree{
	protected List<ASTree> children;
	
	public ASTList(List<ASTree> children) {
		this.children = children;
	}

	@Override
	public ASTree child(int i) {
		return children.get(i);
	}

	@Override
	public int numOfChildren() {
		return children.size();
	}

	@Override
	public Iterator<ASTree> children() {
		return children.iterator();
	}

	@Override
	public String location() {
		String ret;
		for(ASTree ast : children) {
			ret = ast.location();
			if(ret != null)
				return ret;
		}
		return null;
	}
	
	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder();
		String sep = "";
		ret.append("(");
		for(ASTree ast : children) {
			ret.append(sep + ast.toString());
			sep = " ";
		}
		ret.append(")");
		return ret.toString();
	}
	
	public static ASTree createASTree(List<ASTree> list) {
		if(list.size() == 1)
			return list.get(0);
		else
			return new ASTList(list);
	}

}
