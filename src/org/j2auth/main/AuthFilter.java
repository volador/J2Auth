package org.j2auth.main;
/**
 * 过滤器接口
 * @author volador
 *
 */
public interface AuthFilter {
	/**
	 * 过滤器进行处理
	 * @param info 请求上下文
	 * @param chain 过滤链
	 * @return 处理后的请求上下文
	 */
	AuthContext process(AuthContext info,AuthChain chain);
}
