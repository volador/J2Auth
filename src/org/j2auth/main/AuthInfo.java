package org.j2auth.main;
/**
 * 请求上下文接口
 * @author volador
 *
 */
public interface AuthInfo {
	
	//帐号在session中的key
	public static final String SESSION_ACCOUNT_KEY = "i_session_account";
	
	/**
	 * 获取请求用户帐号
	 * @return 帐号
	 */
	String getAccount();
	/**
	 * 获取指定cookie值
	 * @param name cookie键
	 * @return cookie值
	 */
	String getCookie(String name);
	/**
	 * 设置上下文中用户帐号
	 * @param account 帐号
	 */
	void setAccount(String account);
	/**
	 * 删除cookie
	 * @param key cookie键
	 */
	void delCookie(String key);
}