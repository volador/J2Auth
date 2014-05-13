package org.j2auth.ioc.inject;

import org.j2auth.ioc.BeanFactory;
/**
 * 屏蔽接口复杂度，提供当前生产环境
 * @author volador
 *
 */
public abstract class AbstractInjector implements Injector{
	//生产环境
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
