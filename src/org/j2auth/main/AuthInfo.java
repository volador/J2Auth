package org.j2auth.main;

/**
 * ����Ȩ�ް�鴫�ݵ�����
 * @author volador
 *
 */
public interface AuthInfo {
	
	// �û�����session�еļ�
	public static final String SESSION_ACCOUNT_KEY = "i_session_account";
	
	/**
	 * ��ȡ�������������û���¼���˺�
	 */
	String getAccount();
	
	
	/**
	 * ��ȡĳ��cookieֵ
	 * @param name cookie��
	 * @return cookie��Ӧ��ֵ����û���򷵻ؿ�
	 */
	String getCookie(String name);
}
