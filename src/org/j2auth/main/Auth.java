package org.j2auth.main;

/**
 * 定义权控入口
 * @author volador
 *
 */
public interface Auth{
	/**
	 * 启动权控
	 * @param info 请求上下文信息
	 * @return 权限控制后的上下文信息
	 */
	AuthInfo doAuth(AuthInfo info);
}
