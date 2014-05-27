package org.j2auth.filters;
/**
 * 链接用户系统接口
 * @author volador
 *
 */
public interface UserService {

	/**
	 * 判断用户是登录成功
	 * @param account 帐号
	 * @param password 密码
	 * @return true/false
	 */
	boolean check(String account,String password);
}
