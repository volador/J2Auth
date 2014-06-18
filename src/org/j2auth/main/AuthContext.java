package org.j2auth.main;
/**
 * 请求上下文接口
 * @author volador
 *
 */
public interface AuthContext {
	
	//auth上下文在session中的key
	public static final String SESSION = "j_auth_session";
	
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
	 * 删除cookie，path默认值是根（/）
	 * @param key cookie键
	 */
	void delCookie(String key);
	/**
	 * 删除cookie
	 * @param key cookie键
	 * @param path cookie的路径
	 */
	void delCookieWithPath(String key, String path);
}