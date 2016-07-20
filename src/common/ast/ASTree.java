package common.ast;

import java.util.Iterator;

import common.env.Environment;

public abstract class ASTree implements Iterable <ASTree> {
	public abstract ASTree child(int i);
	public abstract int numOfChildren();
	public abstract Iterator<ASTree> children();
	public abstract String location() ;
	public abstract Object eval(Environment env);
	@Override
	public Iterator<ASTree> iterator() {
		return children();
	}
	
}
