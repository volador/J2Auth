package org.j2auth.main;

import org.j2auth.main.AuthInfo;

/**
 * ����ÿһ�������������
 * @author volador
 *
 */ 
public interface Step {
	/**
	 * �������
	 * @param info ��ǰ������
	 * @param chain ��֤��������
	 * @return ��֤�����������Ϣ
	 */
	AuthInfo process(AuthInfo info,DutyChain chain);
}
