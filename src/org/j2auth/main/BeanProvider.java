package org.j2auth.main;

public interface BeanProvider {

	Object getBean(String name);
	
	void close();
}
