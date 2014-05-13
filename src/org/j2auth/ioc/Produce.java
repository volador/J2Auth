package org.j2auth.ioc;
/**
 * 生产者所需要继承的接口
 * @author volador
 *
 */
public interface Produce {
	/**
	 * 根据注入链进行生产
	 * @return 生产所得产品
	 */
	Object doProduce();
}
