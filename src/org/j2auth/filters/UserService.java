package org.j2auth.filters;

import java.util.Set;

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

	/**
	 * 获取用户的checkpoint集合
	 * @param account 用户account
	 * @return checkpoint集合
	 */
	Set<String> fetchUserCheckPoints(String account);

	Set<String> getchResourceCheckPoints();
}
