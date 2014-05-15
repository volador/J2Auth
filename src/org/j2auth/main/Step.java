package org.j2auth.main;

import org.j2auth.main.AuthInfo;

/**
 * 定义每一步操作链的入口
 * @author volador
 *
 */ 
public interface Step {
	/**
	 * 操作入口
	 * @param info 当前上下文
	 * @param chain 验证者责任链
	 * @return 验证后的上下文信息
	 */
	AuthInfo process(AuthInfo info,DutyChain chain);
}
