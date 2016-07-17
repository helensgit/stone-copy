package common.ast;

import common.token.Token;

public class StringLiteral extends ASTLeaf {

	public StringLiteral(Token token) {
		super(token);
	}

	public int value() {
		return getToken().getNumber();
	}
}
