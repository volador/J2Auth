package org.j2auth.main;

/**
 * ����Ȩ�����
 * @author volador
 *
 */
public interface Auth{
	/**
	 * ����Ȩ��
	 * @param info ������������Ϣ
	 * @return Ȩ�޿��ƺ����������Ϣ
	 */
	AuthInfo doAuth(AuthInfo info);
}
