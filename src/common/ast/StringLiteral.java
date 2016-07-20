package common.ast;

import common.env.Environment;
import common.token.Token;

public class StringLiteral extends ASTLeaf {

	public StringLiteral(Token token) {
		super(token);
	}

	public String value() {
		return getToken().getText();
	}

	@Override
	public Object eval(Environment env) {
		return value();
	}
	
	@Override
	public String toString() {
		return "stringliteral:" + super.toString();
	}
	
}
