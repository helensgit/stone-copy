package common.ast;

import common.env.Environment;
import common.token.Token;

public class NumberLiteral extends ASTLeaf {

	public NumberLiteral(Token token) {
		super(token);
	}

	public int value() {
		return Integer.parseInt(getToken().getText());
	}

	@Override
	public Object eval(Environment env) {
		return value();
	}
	
	@Override
	public String toString() {
		return "numberliteral:" + super.toString();
	}
}
