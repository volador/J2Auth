package org.j2auth.main;

import org.j2auth.ioc.BeanFactory;
import org.j2auth.ioc.BeansManager;


/**
 * 简单适配器
 * @author volador
 *
 */
public class LocalIOCAdapter extends AbstractBeanProvider{

	private BeanFactory beans = null;
	
	@Override
	public Object getBean(String name) {
		return beans.getBean(name);
	}
	
	@Override
	public void doInit() {
		//可以在这里更改fileLoader，以及给fileLoader传递参数
		beans = new BeansManager(getInitParams());
	}
	
	@Override
	public void close() {
		beans.clean();
	}
}
