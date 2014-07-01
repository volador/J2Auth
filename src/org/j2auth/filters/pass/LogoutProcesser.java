package org.j2auth.filters.pass;

import org.j2auth.main.AuthChain;
import org.j2auth.main.AuthContext;
import org.j2auth.main.AuthFilter;

/**
 * 处理登出操作：
 * 	1.清理session
 * 	2.清理cookie
 * 	3.重新跳转url
 * @author volador
 *
 */
public class LogoutProcesser implements AuthFilter{
	/*
	 * 检查点，若请求的url与此检查点匹配，则请求被该filter拦截
	 */
	private static String checkUrl = "j_auth_logout";
	/*
	 * 处理成功后重新跳转到的url
	 */
	private static String redirect = null;
	
	@Override
	public AuthContext process(AuthContext info, AuthChain chain) {
		if(checkRequest(info)){
			//清空用户信息并设置跳转url
			info.clear();
			info.directTo(redirect);
		}
		return chain.next(info);
	}
	
	/**
	 * 检查请求是否应该被拦截
	 * @param info 上下文
	 * @return true/false
	 */
	protected boolean checkRequest(AuthContext info){
		if(checkUrl.startsWith("/")){
			String contextPath = "";
			try {
				contextPath = (String) info.get("contextPath");
			} catch (Exception e) {
				e.printStackTrace();
			}
			String requestURI = "";
			try {
				requestURI = (String) info.get("requestURI");
			} catch (Exception e) {
				e.printStackTrace();
			}
			//绝对路径
			String path = contextPath + checkUrl.substring(1);
			return requestURI.equals(path);
		}else{
			//相对路径
			String path = "";
			try {
				path = (String) info.get("servletPath");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return path.endsWith(checkUrl);
		}
	}

	public static void setCheckUrl(String checkUrl) {
		LogoutProcesser.checkUrl = checkUrl;
	}

	public static void setRedirect(String redirect) {
		LogoutProcesser.redirect = redirect;
	}
}
