package common.element;

import common.env.Environment;
import common.env.NestedEnv;

public class Function {
	protected ParameterList parameters;
	protected BlockStament body;
	protected Environment outer;
	
	public Function(ParameterList parameters, BlockStament body, Environment env) {
		this.parameters = parameters;
		this.body = body;
		this.outer = env;
	}
	
	public ParameterList parameters() {
		return parameters;
	}
	
	public BlockStament body() {
		return body;
	}
	
	public Environment makeEnv() {
		return new NestedEnv(outer);
	}
	
	@Override
	public String toString() {
		return "<funtion:" + hashCode() + ">";
	}
}
