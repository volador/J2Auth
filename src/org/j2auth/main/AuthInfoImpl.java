package org.j2auth.main;

import java.util.HashMap; 
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * AuthInfo接口的实现
 * 
 * @author volador
 * 
 */
public class AuthInfoImpl implements AuthInfo {

	private String account;

	private Map<String, Cookie> cookies = null;

	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	private HttpSession session = null;
	
	public AuthInfoImpl(HttpServletRequest request,HttpServletResponse response) {
		this.session = request.getSession(true);
		this.request = request;
		this.response = response;
		
		this.account = (String) session.getAttribute(SESSION_ACCOUNT_KEY);
	}

	@Override
	public String getAccount() {
		return account;
	}

	@Override
	public String getCookie(String name) {
		//使用时才初始化cookie
		if(this.cookies == null) fillCookie();
		Cookie target = cookies.get(name);
		return target == null ? null : target.getValue();
	}
	
	/**
	 * 初始化cookie
	 */
	private void fillCookie(){
		this.cookies = new HashMap<String, Cookie>();
		Cookie[] cs = this.request.getCookies();
		if (cs != null && cs.length > 0) {
			for (Cookie c : cs) {
				this.cookies.put(c.getName(), c);
			}
		}
	}

	@Override
	public void setAccount(String account) {
		this.account = account;
	}

	@Override
	public void delCookie(String key) {
		Cookie c = this.cookies.get(key);
		if(c != null){
			c.setMaxAge(0);
			c.setPath("/");
			this.response.addCookie(c);
		}
	}
}
