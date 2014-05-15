package org.j2auth.main;

import org.j2auth.main.AuthInfo;

/**
 * 责任链，在验证者之间传递
 * @author volador
 *
 */
public interface DutyChain {
	/**
	 * 将责任传递到顺序的下一个验证者
	 * @param info 当前上下文环境
	 */
	AuthInfo next(AuthInfo info);
	/**
	 * 将责任传递到跳跃的下个验证者
	 * @param info 当前上下文环境
	 * @param nextProvider 下个验证者的标号，若下个验证者不存在，终止验证链
	 */
	AuthInfo next(AuthInfo info,String nextProvider);
}
