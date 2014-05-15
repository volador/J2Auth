package org.j2auth.main;

/**
 * 定义权限版块传递的数据
 * @author volador
 *
 */
public interface AuthInfo {
	
	// 用户名在session中的键
	public static final String SESSION_ACCOUNT_KEY = "i_session_account";
	
	/**
	 * 获取请求上下文中用户登录的账号
	 */
	String getAccount();
	
	
	/**
	 * 获取某个cookie值
	 * @param name cookie键
	 * @return cookie对应的值，若没有则返回空
	 */
	String getCookie(String name);
}
