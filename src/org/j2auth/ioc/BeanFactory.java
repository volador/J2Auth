package org.j2auth.ioc;

public interface BeanFactory {

	Object getBean(String name);
	

	boolean clean();
}
