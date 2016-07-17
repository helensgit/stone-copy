package common.env;

import java.util.HashMap;

public class BasicEnv implements Environment{
	private HashMap <String, Object> values;
	
	public BasicEnv() {
		values = new HashMap<String, Object>();
	}
	
	@Override
	public void put(String name, Object object) {
		values.put(name, object);
	}

	@Override
	public Object get(String name) {
		return values.get(name);
	}

}
