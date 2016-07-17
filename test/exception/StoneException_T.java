package exception;

import static org.junit.Assert.*;

import org.junit.Test;

public class StoneException_T {

	@Test
	public void test() {
		throw new StoneException("this is an exception");
	}

}
