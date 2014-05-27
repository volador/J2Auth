package org.j2auth.ioc;
/**
 * bean生产器接口
 * @author volador
 *
 */
public interface Produce {

	/**
	 * 根据bean配置环境生产bean实例，并解决bean依赖注入。
	 * @return bean实例
	 */
	Object doProduce();
}
