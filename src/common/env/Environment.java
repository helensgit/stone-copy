package common.env;

public interface Environment {
	public void put(String name, Object object);
	public Object get(String name);
}
