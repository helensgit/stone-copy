package common.ast;

import common.token.Token;

public class NumberLiteral extends ASTLeaf {

	public NumberLiteral(Token token) {
		super(token);
	}

	public int value() {
		return Integer.parseInt(getToken().getText());
	}
}
