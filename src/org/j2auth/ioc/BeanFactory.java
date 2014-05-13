package org.j2auth.ioc;
/**
 * bean工厂
 * @author volador
 *
 */
public interface BeanFactory {
	/**
	 * 根据名字获取bean
	 * @param name bean的名字
	 * @return bean实例
	 */
	Object getBean(String name);
	
	/**
	 * close this BeanFactory
	 * @return
	 */
	boolean clean();
}
