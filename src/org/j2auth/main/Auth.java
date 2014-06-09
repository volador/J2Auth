package org.j2auth.main;
/**
 * 校验的接口
 * @author volador
 *
 */
public interface Auth{
	/**
	 * 对请求上下文进行权限校验
	 * @param info 上下文
	 * @return 带有校验信息的上下文
	 */
	AuthContext doAuth(AuthContext info);
}
