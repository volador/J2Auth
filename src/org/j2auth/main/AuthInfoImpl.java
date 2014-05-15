package org.j2auth.main;

import java.util.HashMap; 
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * AuthInfo接口的实现
 * 
 * @author volador
 * 
 */
public class AuthInfoImpl implements AuthInfo {

	private String account;

	private Map<String, Cookie> cookies = new HashMap<String, Cookie>();

	public AuthInfoImpl(HttpServletRequest request,HttpSession session) {
		this.account = (String) session.getAttribute(SESSION_ACCOUNT_KEY);
		Cookie[] cs = request.getCookies();
		if (cs != null && cs.length > 0) {
			for (Cookie c : cs) {
				this.cookies.put(c.getName(), c);
			}
		}
	}

	@Override
	public String getAccount() {
		return account;
	}

	@Override
	public String getCookie(String name) {
		Cookie target = cookies.get(name);
		return target == null ? null : target.getValue();
	}
}
