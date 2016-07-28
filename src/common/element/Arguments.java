package common.element;

import java.util.List;

import common.ast.ASTree;
import common.env.EnvEx;
import common.env.Environment;
import exception.StoneException;

public class Arguments extends Postfix {

	public Arguments(List <ASTree> children) {
		super(children);
	}
	
	public static Arguments createASTree(List <ASTree> children) {
		return new Arguments(children);
	}
	
	public int size() {
		return numOfChildren();
	}

	@Override
	public Object eval(Environment env, Object value) {
		if(!(value instanceof Function))
			throw new StoneException("bad function: " + value.getClass(), this);
		Function func = (Function)value;
		ParameterList params = func.parameters();
		if(size() != params.size()) 
			throw new StoneException("bad number of argument", this);
		Environment newEnv = func.makeEnv();
		int num = 0;
		for(ASTree t: this) 
			params.eval((EnvEx)newEnv, num++, ((ASTree)t).eval(env));
		return (func.body()).eval(newEnv);
	}
	
	@Override
	public String toString() {
		return "args...";
	}

}
