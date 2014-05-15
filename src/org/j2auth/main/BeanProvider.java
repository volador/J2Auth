package org.j2auth.main;
/**
 *  定义bean的获取规则
 * @author volador
 *
 */
public interface BeanProvider {
	/**
	 * 通过名字获取bean
	 * @param name bean实例在容器里面声明的名字
	 * @return bean实例
	 */
	Object getBean(String name);
	
	/**
	 * 关闭容器，系统关闭时调用
	 */
	void close();
}
