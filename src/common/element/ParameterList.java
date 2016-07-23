package common.element;

import java.util.List;

import common.ast.ASTLeaf;
import common.ast.ASTList;
import common.ast.ASTree;
import common.env.EnvEx;

public class ParameterList extends ASTList{

	public ParameterList(List<ASTree> children) {
		super(children);
	}
	
	public static ASTree createASTree(List <ASTree> list) {
		return new ParameterList(list);
	}
	
	public void eval(EnvEx env, int index, Object value) {
		env.putNew(name(index), value);
	}
	
	private String name(int index) {
		return ((ASTLeaf)child(index)).getToken().getText();
	}

	public int size() {
		return numOfChildren();
	}
}
