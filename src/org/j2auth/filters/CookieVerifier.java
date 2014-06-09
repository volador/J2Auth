package org.j2auth.filters;

import org.j2auth.main.AuthChain;  
import org.j2auth.main.AuthFilter;
import org.j2auth.main.AuthContext;
import org.j2auth.util.Decoder;
/**
 * cookie校验器：若用户未登录，尝试从cookie中登录
 * @author volador
 *
 */
public class CookieVerifier implements AuthFilter{

	//用户帐号在cookie中的key
	public static final String USER_ACCOUNT = "j_c_user_account";
	//用户密码在cookie中的key
	public static final String USER_PASSWORD = "j_c_user_password";
	
	//存在cookie中的用户信息可能是经过加密的，需要配套解密器
	private Decoder decoder = null;
	
	//验证用户信息
	private UserService userService = null;
	
	//setter
	public void setDecoder(Decoder decoder){
		this.decoder = decoder;
	}
	
	public void setUserService(UserService userService){
		this.userService = userService;
	}
	
	@Override
	public AuthContext process(AuthContext info, AuthChain chain) {
		if(isLogin(info)){
			//如果已经登录，则不做处理，处理链往下
			return chain.next(info);
		}
		
		String account = info.getCookie(USER_ACCOUNT);
		String password = info.getCookie(USER_PASSWORD);
		
		if(noValue(account) && noValue(password)){
			//若没有相关cookie信息，不做处理，处理链往下
			return chain.next(info);
		}
		
		//如果配置了解密器，用解密器处理cookie中用户信息
		if(this.decoder != null){
			password = this.decoder.decode(password);
		}
		
		//用户cookie信息进行登录：注意爆破入侵（无限枚举帐号密码进行爆破），对单个ip进行限制检验次数。
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
	 * cookie登录成功后的处理
	 * @param account 成功的帐号
	 * @param info 上下文
	 */
	protected void cookieLoginSuccess(String account,AuthContext info){
		info.setAccount(account);
	}

	private boolean noValue(String value){
		return value == null || value.length() == 0;
	}
	
	protected boolean isLogin(AuthContext info){
		return null == info.getAccount();
	}

}
