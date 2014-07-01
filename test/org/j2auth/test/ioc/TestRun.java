package org.j2auth.test.ioc;

import static org.junit.Assert.*; 

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.j2auth.ioc.BeanFactory;
import org.j2auth.ioc.BeansManager;
import org.j2auth.test.ioc.beans.Person;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestRun {

	private static BeanFactory beans;
	
	@BeforeClass
	public static void setUp(){
		Map<String,String> params = new HashMap<String,String>();
		params.put("beans_file_name","test_beans.xml");
		params.put("beans_file_path","/data/pros/auth/J2Auth/test/org/j2auth/test/ioc");
		beans = new BeansManager(params);
	}

	@Test
	public void testGetObject() {
		Person p = (Person) beans.getBean("person");
		Person p_not_exit = (Person) beans.getBean("Person_x");
		assertNotNull(p);
		assertNull(p_not_exit);
	}
	
	@Test
	public void testStringTag(){
		Person p = (Person)beans.getBean("person");
		assertNotNull(p);
		String name = p.getName();
		assertEquals("voladorzhang", name);
	}
	
	@Test
	public void testObjectMethod(){
		Object o = new A();
		Method[] ms = o.getClass().getDeclaredMethods();
		for(Method m : ms){
			System.out.println(m);
		}
	}
}

class A{
	public void method(){
		
	}
}
