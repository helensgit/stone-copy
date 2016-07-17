package common.ast;

import java.util.ArrayList;
import java.util.Iterator;

import common.token.Token;

public class ASTLeaf extends ASTree {
	private static ArrayList <ASTree> empty = new ArrayList<ASTree>();
	private Token token;
	
	public ASTLeaf(Token token) {
		this.token = token;
	}

	@Override
	public ASTree child(int i) {
		throw new IndexOutOfBoundsException();
	}

	@Override
	public int numOfChildren() {
		return 0;
	}

	@Override
	public Iterator<ASTree> children() {
		return empty.iterator();
	}

	@Override
	public String location() {
		return "at line " + token.getLineNumber();
	}

	public Token getToken() {
		return token;
	}
	
	@Override
	public String toString() {
		return token.getText();
	}
}
