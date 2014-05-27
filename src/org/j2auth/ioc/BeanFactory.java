package org.j2auth.ioc;
/**
 * Baen工厂接口
 * @author volador
 *
 */
public interface BeanFactory {

	/**
	 * 根据名字获取bean实例
	 * @param name bean实例名字引用
	 * @return bean实例
	 */
	Object getBean(String name);
	/**
	 * 关闭bean容器
	 * @return true/false
	 */
	boolean clean();
}
