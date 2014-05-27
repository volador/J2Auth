package org.j2auth.ioc.inject;

import org.j2auth.ioc.BeanFactory;
import org.w3c.dom.Node; 
/**
 * 依赖链注入器接口
 * @author volador
 *
 */
public interface Injector {

	/**
	 * 对bean进行以来注入
	 * @param obj bean实例
	 * @param factory bean工厂
	 * @return 注入后的bean实例
	 */
	Object doInject(Object obj,BeanFactory factory);

	/**
	 * 依赖信息
	 * @param node bean.xml中当前bean定义节点
	 * @param currentPath bean.xml中当前bean定义节点路径
	 */
	void nodeInfo(Node node,String currentPath);
	
}
