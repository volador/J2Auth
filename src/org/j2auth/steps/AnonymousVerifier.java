package org.j2auth.steps;

import org.j2auth.main.AuthChain; 
import org.j2auth.main.AuthInfo;
import org.j2auth.main.Step;

public class AnonymousVerifier implements Step{

	public static final String ANONYMOUS_ACCOUNT = "j_anonymous";
	
	@Override
	public AuthInfo process(AuthInfo info, AuthChain chain) {
		if(isAnonymous(info)){
			return chain.next(info);
		}
		info.setAccount(ANONYMOUS_ACCOUNT);
		return chain.next(info);
	}
	
	protected boolean isAnonymous(AuthInfo info){
		return null == info.getAccount();
	}
}
