package org.j2auth.steps;
/**
 * ��ȡ�û���Ϣ�ı�׼�������м��� �������ݿ��ȡ���ļ���ȡ�������ȡ�ȵ�
 * @author volador
 *
 */
public interface UserService {
	/**
	 * �ж��û��Ƿ����
	 * @param account �û��ʺ�
	 * @param password �û�����
	 * @return true/false
	 */
	boolean check(String account,String password);
}
