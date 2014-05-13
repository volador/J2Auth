package org.j2auth.ioc.inject;

import org.j2auth.ioc.BeanFactory;
import org.w3c.dom.Node; 

/**
 * 定义注入器规范
 * @author volador
 *
 */
public interface Injector {
	/**
	 * 进行注入操作
	 * @param obj 注入前
	 * @param proEvn 生产环境，用于解决依赖
	 * @return 注入后的实例
	 */
	Object doInject(Object obj,BeanFactory factory);
	/**
	 * 根据传递进来的node，保存注入时所需要的信息
	 * @param node 属于自己处理的node
	 */
	void nodeInfo(Node node,String currentPath);
	
}
