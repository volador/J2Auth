package org.j2auth.main;

import java.io.IOException;
import java.util.HashMap; 
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 * 请求上下文实现
 * @author volador
 *
 */
public class AuthContextImpl implements AuthContext {

	private String account;

	private Map<String, Cookie> cookies = null;

	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	private HttpSession session = null;

	private boolean redirectStatus = false;
	private String redirectUrl = null;

	private Set<String> resourceCheckPoints = new HashSet<String>();

	private Set<String> userCheckPoints = new HashSet<String>();
	
	private static final String SESSION_ACCOUNT_KEY = "j_auth_session_account_key";
	
	public AuthContextImpl(HttpServletRequest request,HttpServletResponse response) {
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
		//初次调用填充cookie数组
		if(this.cookies == null) fillCookie();
		Cookie target = cookies.get(name);
		return target == null ? null : target.getValue();
	}
	
	/**
	 * 填充cookie数组
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
		this.session.setAttribute(SESSION_ACCOUNT_KEY, account);
	}

	@Override
	public void delCookieWithPath(String key, String path) {
		//初次调用填充cookie数组
		if(this.cookies == null) fillCookie();
		Cookie c = this.cookies.get(key);
		if(c != null){
			c.setMaxAge(0);
			c.setPath(path);
			this.response.addCookie(c);
		}
	}
	
	@Override
	public void delCookie(String key) {
		String path = "/";
		this.delCookieWithPath(key, path);
	}
	
	@Override
	public HttpServletRequest getRequest() {
		return this.request;
	}
	
	@Override
	public String toString() {
		return this.account;
	}
	
	@Override
	public void stopAndRedirect(String redirect) {
		try {
			this.response.sendRedirect(redirect);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void clear() {
		//清理session
		this.account = null;
		this.session.removeAttribute(SESSION_ACCOUNT_KEY);
		//清理cookie
		this.delCookie(COOKIE_USER_ACCOUNT);
		this.delCookie(COOKIE_USER_PASSWORD);
	}
	
	@Override
	public boolean needRedirect() {
		return this.redirectStatus;
	}
	
	@Override
	public String getRedirectUrl() {
		return this.redirectUrl;
	}
	
	@Override
	public void setRedirectUrl(String url) {
		//后面的设置不能把前面覆盖
		if(!this.redirectStatus){
			this.redirectStatus = true;
			this.redirectUrl = url;
		}
	}
	
	@Override
	public Set<String> getResourceCheckPoints() {
		return this.resourceCheckPoints;
	}
	
	@Override
	public Set<String> getUserCheckPoints() {
		return this.userCheckPoints;
	}
	
	@Override
	public void setResourceCheckPoints(Set<String> checkPoints) {
		this.resourceCheckPoints.addAll(checkPoints);
	}
	
	@Override
	public void setUserCheckPoints(Set<String> checkPoints) {
		this.userCheckPoints.addAll(checkPoints);
	}
	
	@Override
	public void setUserCheckPoint(String checkPoint) {
		this.userCheckPoints.add(checkPoint);
	}
	
	@Override
	public void setResourceCheckPoint(String checkPoint) {
		this.resourceCheckPoints.add(checkPoint);
	}
}
