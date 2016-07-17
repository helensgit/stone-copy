package common.ast;

import common.token.Token;

public class Name extends ASTLeaf {

	public Name(Token token) {
		super(token);
	}

	public String name() {
		return getToken().getText();
	}
}
