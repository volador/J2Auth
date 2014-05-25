package org.j2auth.steps;

import org.j2auth.main.AuthChain; 
import org.j2auth.main.AuthFilter;
import org.j2auth.main.AuthInfo;
import org.j2auth.util.Decoder;

public class CookieVerifier implements AuthFilter{

	public static final String USER_ACCOUNT = "j_c_user_account";
	public static final String USER_PASSWORD = "j_c_user_password";
	
	private Decoder decoder = null;
	
	private UserService userService = null;
	
	//setter
	public void setDecoder(Decoder decoder){
		this.decoder = decoder;
	}
	
	public void setUserService(UserService userService){
		this.userService = userService;
	}
	
	@Override
	public AuthInfo process(AuthInfo info, AuthChain chain) {
		if(isLogin(info)){
			return chain.next(info);
		}
		
		String account = info.getCookie(USER_ACCOUNT);
		String password = info.getCookie(USER_PASSWORD);
		
		if(noValue(account) && noValue(password)){
			return chain.next(info);
		}
		
		if(this.decoder != null){
			password = this.decoder.decode(password);
		}
		
		boolean result = userService.check(account,password);
		if(result){
			cookieLoginSuccess(account,info);
		}else{
			info.delCookie(USER_ACCOUNT);
			info.delCookie(USER_PASSWORD);
		}
		
		return chain.next(info);
	}
	
	protected void cookieLoginSuccess(String account,AuthInfo info){
		info.setAccount(account);
	}

	private boolean noValue(String value){
		return value == null || value.length() == 0;
	}
	
	protected boolean isLogin(AuthInfo info){
		return null == info.getAccount();
	}

}
