package org.j2auth.ioc;
/**
 * bean����
 * @author volador
 *
 */
public interface BeanFactory {
	/**
	 * �������ֻ�ȡbean
	 * @param name bean������
	 * @return beanʵ��
	 */
	Object getBean(String name);
	
	/**
	 * close this BeanFactory
	 * @return
	 */
	boolean clean();
}
