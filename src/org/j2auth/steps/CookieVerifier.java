package org.j2auth.steps;

import org.j2auth.main.AuthInfo;
import org.j2auth.main.DutyChain;
import org.j2auth.main.Step;
import org.j2auth.util.Decoder;
/**
 * 从cookie中验证用户信息
 * @author volador
 *
 */
public class CookieVerifier implements Step{

	//j2auth存cookie以"j_c"开头
	public static final String USER_ACCOUNT = "j_c_user_account";
	public static final String USER_PASSWORD = "j_c_user_password";
	
	//用于密码解密 存cookie中的密码可能是经过加密的
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
	public AuthInfo process(AuthInfo info, DutyChain chain) {
		//如果已经登录，不做处理
		if(isLogin(info)){
			return chain.next(info);
		}
		
		//根据cookie中存的用户名字密码进行登录
		String account = info.getCookie(USER_ACCOUNT);
		String password = info.getCookie(USER_PASSWORD);
		
		//cookie是否有验证信息
		if(noValue(account) && noValue(password)){
			return chain.next(info);
		}
		
		//密码解密
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
	
	/**
	 * cookie登录成功后的处理逻辑，可以扩展，比如增加cookie生存时间等
	 * @param account 用户登录帐号
	 * @param info 上下文
	 */
	protected void cookieLoginSuccess(String account,AuthInfo info){
		info.setAccount(account);
	}
	/*
	 * 判断值非空
	 */
	private boolean noValue(String value){
		return value == null || value.length() == 0;
	}
	
	/**
	 * 判断用户是否已经登录，如果上下文中有用户信息(session)，表示用户已经登录过了
	 * @param info 请求上下文
	 * @return true/false
	 */
	protected boolean isLogin(AuthInfo info){
		return null == info.getAccount();
	}

}
