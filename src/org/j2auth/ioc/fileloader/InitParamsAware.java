package org.j2auth.ioc.fileloader;

import java.util.Map;

/**
 * ʵ�ָýӿڵ�����Ҫ��ȡ��������
 * @author volador
 *
 */
public interface InitParamsAware {
	/**
	 * ������������
	 */
	void setInitParams(Map<String,String> params);
}
