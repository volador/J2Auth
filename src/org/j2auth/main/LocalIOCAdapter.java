package org.j2auth.main;

import org.j2auth.ioc.BeanFactory;
import org.j2auth.ioc.BeansManager;
/**
 * IOC适配器，将本地实现的IOC容器接入系统
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
		beans = new BeansManager(getInitParams());
	}
	
	@Override
	public void close() {
		beans.clean();
	}
}
