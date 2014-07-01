package org.j2auth.filters.pass;

import org.j2auth.filters.UserService;
import org.j2auth.main.AuthChain;
import org.j2auth.main.AuthContext;
import org.j2auth.main.AuthDirect;
import org.j2auth.main.AuthFilter;
/**
 * form登录[单点]
 * @author volador
 *
 */
public class FormLoginChecker implements AuthFilter{

	//拦截form提交
	private static String formAction = "j_auth_form_login";
	//form-account
	private static String accountName = "j_auth_form_login_account";
	//form-password
	private static String passwordName = "j_auth_form_login_password";
	//登录成功后跳转页面
	private static String successGoTo = "login/success";
	//登录失败后跳转页面
	private static String failGoTo = "login.jsp";
	
	//用于多点登录
	private static final String TAG_NAME = "j_auth_form_login_tag";
	private static String expectTag = null;
	
	private UserService userService = null;
	
	@Override
	public AuthContext process(AuthContext info, AuthChain chain) {
		if(checkRequest(info)){
			//获取account/password 并且校验
			String account = "";
			try {
				account = (String) info.get("parameter",accountName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String password = "";
			try {
				password = (String) info.get("parameter",passwordName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//验证身份,并做跳转
			if(userService.check(account,password)){
				info.directTo(successGoTo);
			}else{
				//forword 复用request
				info.directTo(failGoTo,AuthDirect.FORWORD);
			}
		}
		return chain.next(info);
	}
	
	/**
	 * 检查请求是否被拦截,如果有expectTag，则请求参数中必须带与expectTag想匹配内容，才能被拦截
	 * @param info 上下文
	 * @return true/false
	 */
	protected boolean checkRequest(AuthContext info){
		if(expectTag != null){
			String tag = null;
			try {
				tag = (String) info.get("parameter",TAG_NAME);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(tag == null || !tag.equals(expectTag)){
				return false;
			}
		}
		
		if(formAction.startsWith("/")){
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
			String path = contextPath + formAction.substring(1);
			return requestURI.equals(path);
		}else{
			String servletPath = "";
			try {
				servletPath = (String) info.get("servletPath");
			} catch (Exception e) {
				e.printStackTrace();
			}
			String path = servletPath;
			return path.endsWith(formAction);
		}
	}
	
	public void setUserService(UserService userService){
		this.userService = userService;
	}
}
