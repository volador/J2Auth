package org.j2auth.filters;

import java.util.Set;

import org.j2auth.main.AuthChain;
import org.j2auth.main.AuthContext;
import org.j2auth.main.AuthFilter;
import org.j2auth.util.Debug;

/**
 * 根据上下文信息，判决请求是否通过
 * @author volador
 *
 */
public class JudgeProcesser implements AuthFilter {
	
	//授权失败后的导向[未登录]
	private String loginPage = null;
	//授权失败后的导向[已经登录]
	private String forbidenPage = null;
	
	@Override
	public AuthContext process(AuthContext info, AuthChain chain) {		
		Set<String> userCheckPoints = info.getUserCheckPoints();
		Set<String> resourceCheckPoints = info.getResourceCheckPoints();
		if(!info.needRedirect() && !judgeLogic(userCheckPoints, resourceCheckPoints)){
			if(hasLogin(info)){
				//default forbiden page is login page.
				forbidenPage = forbidenPage != null ? forbidenPage : loginPage;
				info.setRedirectUrl(forbidenPage);
			}else{
				info.setRedirectUrl(loginPage);
			}
		}
		return chain.next(info);
	}
	
	private boolean hasLogin(AuthContext info) {
		return !info.getAccount().equals(AuthContext.ANONYMOUS_ACCOUNT);
	}

	/**
	 * 判决算法 当userCP是resourceCP的子集时通过
	 * @param userCheckPoints 用户手上的CPS 
	 * @param resourceCheckPoints 资源所需要的CPS
	 * @return true/false
	 */
	protected boolean judgeLogic(Set<String> userCheckPoints, Set<String> resourceCheckPoints) {
		Debug.show("user-cps:" + userCheckPoints);
		Debug.show("resource-cps:" + resourceCheckPoints);
		boolean result = false;
		//当资源不注册到checkpoint的时候，默认该资源是全封闭的[资源的悲观控制]
		if(resourceCheckPoints != null && resourceCheckPoints.size() > 0){
			int rSize = resourceCheckPoints.size(),index = 0;
			for(String rcp : resourceCheckPoints){
				if(!userCheckPoints.contains(rcp)) break;
				index++;
			}
			if(index == rSize) result = true;
		}
		return result;
	}
	
	public void setLoginPage(String url){
		this.loginPage = url;
	}
	
	public void setForbidenPage(String url){
		this.forbidenPage = url;
	}
}
