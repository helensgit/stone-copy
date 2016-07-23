package common.element;

import java.util.List;

import common.ast.ASTLeaf;
import common.ast.ASTList;
import common.ast.ASTree;
import common.env.EnvEx;
import common.env.Environment;

public class DefStmnt extends ASTList{

	public DefStmnt(List<ASTree> children) {
		super(children);
	}
	
	public String name() {
		return ((ASTLeaf)child(0)).getToken().getText();
	}
	
	public ParameterList parameters() {
		return (ParameterList)child(1);
	}
	public BlockStament body() {
		return (BlockStament)child(2);
	}
	
	@Override
	public String toString() {
        return "(def " + name() + " " + parameters() + " " + body() + ")";
    }
	
	@Override
	public Object eval(Environment env) {
		((EnvEx)env).putNew(name(), new Function(parameters(), body(), env));
		return name();
	}
	
	public static ASTree createASTree(List <ASTree> list) {
		return new DefStmnt(list);
	}
}
