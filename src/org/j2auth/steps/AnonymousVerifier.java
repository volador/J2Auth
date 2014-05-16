package org.j2auth.steps;

import org.j2auth.main.AuthInfo;
import org.j2auth.main.DutyChain;
import org.j2auth.main.Step;
/**
 * 匿名请求校验者 处理匿名请求
 * @author voaldor
 *
 */
public class AnonymousVerifier implements Step{

	public static final String ANONYMOUS_ACCOUNT = "j_anonymous";
	
	@Override
	public AuthInfo process(AuthInfo info, DutyChain chain) {
		if(isAnonymous(info)){
			return chain.next(info);
		}
		info.setAccount(ANONYMOUS_ACCOUNT);
		return chain.next(info);
	}
	
	/**
	 * 判断是否是匿名登录：如果上下文中有account，则不是匿名
	 * @param info 请求上下文
	 * @return true/false
	 */
	protected boolean isAnonymous(AuthInfo info){
		return null == info.getAccount();
	}
}
