package common.env;

public interface EnvEx extends Environment {
	void putNew(String name, Object value);

	Environment where(String name);

	void setOuter(Environment e);
}
