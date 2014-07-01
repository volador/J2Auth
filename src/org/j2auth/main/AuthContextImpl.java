package org.j2auth.main;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap; 
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.j2auth.util.ReflectUtil;
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

	private Set<String> resourceCheckPoints = new HashSet<String>();

	private Set<String> userCheckPoints = new HashSet<String>();

	private boolean directStatus;

	private String directUrl;

	private AuthDirect directType;

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
	public void delCookie(String key, String path) {
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
		this.delCookie(key, COOKIE_PATH);
	}
	
	@Override
	public Object get(String attribute) throws NoSuchAttributeException, GetAttributeException {
		return get(attribute, null);
	}
	@Override
	public Object get(String attribute,Object param ) throws NoSuchAttributeException, GetAttributeException{
		String getter = ReflectUtil.getter(attribute);
		boolean noExitInLocal = false;
		boolean noExitInRequest = false;
		Exception otherwise = null;
		
		try {
			//try to invoke local method
			if(param == null) return invoke(this, getter);
			else return invoke(this, getter, param);
		} catch (NoSuchMethodException e) {
			noExitInLocal = true;
		} catch (Exception e) {
			otherwise = e;
		}
		
		try{
			//try to invoke request method
			if(param == null) return invoke(this.request, getter);
			else return invoke(this.request, getter, param);
		} catch (NoSuchMethodException e) {
			noExitInRequest = true;
		} catch(Exception e){
			otherwise = e;
		}
		
		boolean noExit = noExitInLocal && noExitInRequest;
		if(noExit){
			List<String> classes = new ArrayList<String>();
			classes.add(this.getClass().toString());
			classes.add(this.request.getClass().toString());
			throw new NoSuchAttributeException(classes.toString(), attribute);
		}else{
			throw new GetAttributeException(otherwise);
		}
	}
	
	private Object invoke(Object obj, String method) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Method m = obj.getClass().getDeclaredMethod(method);
		return m.invoke(obj);
	}
	
	private Object invoke(Object obj, String method, Object param) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Class<?> paramType = param.getClass();
		Method m = obj.getClass().getDeclaredMethod(method, paramType);
		return m.invoke(obj, param);
	}
	
	@Override
	public String toString() {
		return this.account + " " + this.directStatus + " " + this.directUrl + " " + this.directType;
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

	@Override
	public void addCookie(String key, String value) {
		Cookie c = new Cookie(key, value);
		c.setPath(COOKIE_PATH);
		c.setMaxAge(COOKIE_MAX_AGE);
		this.response.addCookie(c);
	}

	@Override
	public boolean needDirect() {
		return this.directStatus;
	}

	@Override
	public void directTo(String url) {
		this.directTo(url, AuthDirect.REDIRECT);
	}

	@Override
	public void directTo(String url, AuthDirect type) {
		//只能设定一次
		if(this.directStatus) return;
		this.directUrl = url;
		this.directType = type;
		this.directStatus = true;
	}

	@Override
	public String getDirectUrl() {
		return this.directUrl;
	}

	@Override
	public AuthDirect getDirectType() {
		return this.directType;
	}
}
