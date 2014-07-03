package org.j2auth.main;

import java.util.Set;

/**
 * 请求上下文接口
 * @author volador
 *
 */
public interface AuthContext {
	
	//auth上下文在session中的key
	public static final String SESSION = "j_auth_session";
	
	//用户帐号在cookie中的key
	public static final String COOKIE_USER_ACCOUNT = "j_auth_cookie_account_key";
	//用户密码在cookie中的key
	public static final String COOKIE_USER_PASSWORD = "j_auth_cookie_password_key";
	//cookie路径
	public static final String COOKIE_PATH = "/";
	//cookie生存时间：2周
	public static final int COOKIE_MAX_AGE = 2 * 7 * 24 * 60 * 60;
	
	//给匿名用户分配的帐号
	public static final String ANONYMOUS_ACCOUNT = "j_auth_anonymous";
	
	//重定向
	public static final String DIRECT_URL = "direct_url";
	public static final String DIRECT_TYPE = "direct_type";
	
	/**
	 * 获取请求用户帐号
	 * @return 帐号
	 */
	String getAccount();
	/**
	 * 获取指定cookie值
	 * @param name cookie键
	 * @return cookie值
	 */
	String getCookie(String name);
	/**
	 * 设置上下文中用户帐号
	 * @param account 帐号
	 */
	void setAccount(String account);
	/**
	 * 删除cookie，path默认值是根（/）
	 * @param key cookie键
	 */
	void delCookie(String key);
	/**
	 * 删除cookie
	 * @param key cookie键
	 * @param path cookie的路径
	 */
	void delCookie(String key, String path);
	
	
	/**
	 * 清理上下文中用户信息[session&&cookie中的用户信息]
	 */
	void clear();
	/**
	 * 是否需要重新跳转
	 * @return true/false
	 */
	boolean needDirect();
	/**
	 * 以redirect形式跳转到制定url
	 * @param url 制定url
	 */
	void directTo(String url);
	/**
	 * 用指定方式跳转到指定url，指定方式包括：forword/redirect
	 * @param url 指定跳转url
	 * @param type 指定方式，其中redirect为外部跳转/forword为内部跳转
	 */
	void directTo(String url, AuthDirect type);
	/**
	 * 获取跳转url
	 */
	String getDirectUrl();
	/**
	 * 获取跳转类型
	 */
	AuthDirect getDirectType();
	
	/**
	 * 获取访问用户的checkpoint集合
	 * @return checkpoint集合
	 */
	Set<String> getUserCheckPoints();
	/**
	 * 设置访问用户的checkpoint
	 * @param checkPoints 访问用户的checkpoint集合
	 */
	void setUserCheckPoints(Set<String> checkPoints);
	/**
	 * 给访问用户添加一个checkpoint
	 * @param checkPoint 检查点
	 */
	void setUserCheckPoint(String checkPoint);
	/**
	 * 获取访问资源的checkpoint
	 * @return checkpoint集合
	 */
	Set<String> getResourceCheckPoints();
	/**
	 * 设置访问资源的checkpoint
	 * @param checkPoints 访问资源的checkpoint集合
	 */
	void setResourceCheckPoints(Set<String> checkPoints);
	/**
	 * 个体访问资源添加一个checkpoint
	 * @param checkPoint 检查点
	 */
	void setResourceCheckPoint(String checkPoint);
	/**
	 * 添加cookie
	 * @param key cookie的key
	 * @param value cookie的value
	 */
	void addCookie(String key, String value);
	/**
	 * 统一上下文资源属性获取入口<br>
	 * 在authcontext中，资源属性包括authcontext本身的属性，以及请求（request）本身的属性，注意：这些属性必须提供可访问的get方法。<br>
	 * authcontext上下文资源属性被抽象成2种类型：<br>
	 * <ul>
	 * 	<li>单键值类型，抽象定义为：Object attributeName = attributeValue;</li>
	 * <li>多键值类型，抽象定义为：Map&lt;Object[param-key],Object[param-value]&gt; attributeNme = attributeValue;</li>
	 * <ul>
	 * <br>
	 * example:
	 * <pre>
	 *		获取上下文中的account属性（单键值类型）：
	 *			<code>String account = (String)get("account");<code> 
	 *		获取上下文中的请求传递过来的account值（多键值类型）：
	 *			<code>String account = (String)get("parameter","account");</code>
	 *		获取session（多键值类型）：
	 *			<code> HttpSession session = (HttpSession)get("session",true);
	 * </pre>
	 * 
	 * 该方法主要作用于多键值属性，用于获取多键值属性中对应键的值。
	 * @param attribute 属性的名字
	 * @param key 对应键
	 * @return 键对应的值
	 * @throws NoSuchAttributeException authcontext&request中都没有该属性（找不到该属性的开放get方法）
	 * @throws GetAttributeException 获取属性值出错，无法访问该属性get方法。
	 */
	Object get(String attribute, Object key) throws NoSuchAttributeException,GetAttributeException;
	/**
	 * 该方法主要用于访问单键值属性
	 * @param attribute 属性名字
	 * @return 属性值
	 * @ses get(String attribute, Object key)
	 */
	Object get(String attribute) throws NoSuchAttributeException, GetAttributeException;
}