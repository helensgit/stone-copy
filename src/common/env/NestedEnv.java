package common.env;

import java.util.HashMap;
import java.util.Map;

public class NestedEnv implements EnvEx{
	protected Environment outer;
	protected Map <String, Object> values;
	
	public NestedEnv() {
		this(null);
	}
	
	public NestedEnv(Environment outer) {
		this.outer = outer;
		values = new HashMap <String, Object> ();
	}
	
	@Override
	public void setOuter(Environment outer) {
		this.outer = outer;
	}

	@Override
	public Environment where(String name) {
		if(values.containsKey(name))
			return this;
		else {
			if(outer == null)
				return null;
			else
				return ((NestedEnv)outer).where(name);
		}
	}
	
	@Override
	public void putNew(String name, Object object) {
		values.put(name, object);
	}
	
	@Override
	public void put(String name, Object object) {
		Environment e = where(name);
		if(e == null)
			e = this;
		((NestedEnv)e).putNew(name, object);
	}

	@Override
	public Object get(String name) {
		Environment env = where(name);
		if(env == null)
			return null;
		else
			return ((NestedEnv)env).values.get(name);
	}

}
