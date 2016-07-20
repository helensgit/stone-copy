package t;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class T_Test {

	private Method method = null;
	public static void test(double i) {
		System.out.println("test double");
	}	
	
	public static void test() {
		System.out.println("no argument");
	}
	
	
	@Test
	public void testReflect() {
		try {
			method = this.getClass().getMethod("test");
			method.invoke(null);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testS() {
		Integer integer = new Integer(12);
		System.out.println(integer.toString());
		System.out.println(String.valueOf(integer));
	}
	
}
