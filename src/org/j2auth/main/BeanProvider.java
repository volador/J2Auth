package org.j2auth.main;
/**
 *  ����bean�Ļ�ȡ����
 * @author volador
 *
 */
public interface BeanProvider {
	/**
	 * ͨ�����ֻ�ȡbean
	 * @param name beanʵ����������������������
	 * @return beanʵ��
	 */
	Object getBean(String name);
	
	/**
	 * �ر�������ϵͳ�ر�ʱ����
	 */
	void close();
}
