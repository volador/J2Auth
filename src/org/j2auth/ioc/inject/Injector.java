package org.j2auth.ioc.inject;

import org.j2auth.ioc.BeanFactory;
import org.w3c.dom.Node; 

/**
 * ����ע�����淶
 * @author volador
 *
 */
public interface Injector {
	/**
	 * ����ע�����
	 * @param obj ע��ǰ
	 * @param proEvn �������������ڽ������
	 * @return ע����ʵ��
	 */
	Object doInject(Object obj,BeanFactory factory);
	/**
	 * ���ݴ��ݽ�����node������ע��ʱ����Ҫ����Ϣ
	 * @param node �����Լ������node
	 */
	void nodeInfo(Node node,String currentPath);
	
}
