package org.j2auth.steps;
/**
 * 获取用户信息的标准：可以有几个 方向：数据库获取、文件获取、缓存获取等等
 * @author volador
 *
 */
public interface UserService {
	/**
	 * 判断用户是否存在
	 * @param account 用户帐号
	 * @param password 用户密码
	 * @return true/false
	 */
	boolean check(String account,String password);
}
