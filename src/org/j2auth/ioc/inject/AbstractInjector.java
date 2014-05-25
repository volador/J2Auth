package org.j2auth.ioc.inject;

import org.j2auth.ioc.BeanFactory;

public abstract class AbstractInjector implements Injector{
	private BeanFactory factory;
	
	protected BeanFactory getBeanFactory(){
		return factory;
	}
	
	@Override
	public Object doInject(Object obj,BeanFactory factory) {
		this.factory = factory;
		return doInject(obj);
	}

	abstract Object doInject(Object obj);
}
