package org.j2auth.main;
/**
 * 系统中的filter以bean形式管理在IOC容器中<br>
 * 可以将现有IOC容器接入系统<br>
 * 该接口定义系统从IOC中获取bean的结构
 * @author volador
 *
 */
public interface BeanProvider {
	/**
	 * 从bean容器中获取bean实例
	 * @param name bean的名字
	 * @return bean实例
	 */
	Object getBean(String name);
	/**
	 * 关闭bean容器，系统关闭时会调用该接口
	 */
	void close();
}
