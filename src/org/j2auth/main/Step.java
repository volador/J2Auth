package org.j2auth.main;

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
