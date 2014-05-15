package org.j2auth.main;

import org.j2auth.main.AuthInfo;

/**
 * ������������֤��֮�䴫��
 * @author volador
 *
 */
public interface DutyChain {
	/**
	 * �����δ��ݵ�˳�����һ����֤��
	 * @param info ��ǰ�����Ļ���
	 */
	AuthInfo next(AuthInfo info);
	/**
	 * �����δ��ݵ���Ծ���¸���֤��
	 * @param info ��ǰ�����Ļ���
	 * @param nextProvider �¸���֤�ߵı�ţ����¸���֤�߲����ڣ���ֹ��֤��
	 */
	AuthInfo next(AuthInfo info,String nextProvider);
}
