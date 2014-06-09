package org.j2auth.filters;

import org.j2auth.main.AuthChain; 
import org.j2auth.main.AuthFilter;
import org.j2auth.main.AuthContext;
/**
 * 匿名校验器：判断请求是否是匿名请求
 * @author volador
 *
 */
public class AnonymousVerifier implements AuthFilter{

	//给匿名用户分配的帐号
	public static final String ANONYMOUS_ACCOUNT = "j_anonymous";
	
	@Override
	public AuthContext process(AuthContext info, AuthChain chain) {
		if(isAnonymous(info)){
			return chain.next(info);
		}
		info.setAccount(ANONYMOUS_ACCOUNT);
		return chain.next(info);
	}
	
	/**
	 * 判断session中是否有用户信息
	 * @param info 上下文
	 * @return true/false
	 */
	protected boolean isAnonymous(AuthContext info){
		return null == info.getAccount();
	}
}
