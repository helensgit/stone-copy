package common.ast;

import common.env.Environment;
import common.token.Token;
import exception.StoneException;

public class Name extends ASTLeaf {

	public Name(Token token) {
		super(token);
	}

	public String name() {
		return getToken().getText();
	}

	@Override
	public Object eval(Environment env) {
		Object ret = env.get(name());
		if(ret == null)
			throw new StoneException("undefined name:" + name(), this);
		else
			return ret;
	}
	
	@Override
	public String toString() {
		return "name:" + super.toString();
	}
}
