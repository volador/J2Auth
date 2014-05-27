package org.j2auth.main;

import org.j2auth.main.AuthInfo;
/**
 * 过滤链接口
 * @author volador
 *
 */
public interface AuthChain {

	/**
	 * 将处理传递到下游过滤器
	 * @param info 请求上下文
	 * @return 处理后的上下文
	 */
	AuthInfo next(AuthInfo info);
	/**
	 * 将处理传递到下游指定的过滤器
	 * @param info 请求上下文
	 * @param nextProvider 下个校验器名字
	 * @return 处理后上下文
	 */
	AuthInfo next(AuthInfo info,String nextProvider);
}