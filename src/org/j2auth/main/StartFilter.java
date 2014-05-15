package org.j2auth.main;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.j2auth.util.ReflectOpException;
import org.j2auth.util.ReflectUtil;

/**
 * 启动权限控制的过滤器
 * 
 * @author voaldor
 * 
 */
public class StartFilter implements Filter {

	// 定义初始化参数在web.xml中的param-name
	public static final String BEAN_PROVIDER_PARAM_NAME = "BeanProvider";
	//authinfo在session中的名字
	public static final String AUTH_INFO_NAME_IN_SESSION = "auth_info";

	// 默认BeanProvider
	private static String DEFAULT_BEAN_PROVIDER = "org.j2auth.bootstrap.LocalIOCAdapter";

	// ioc容器
	protected BeanProvider beanProvider = null;
	protected Auth authManager = null;

	/**
	 * 销毁[关闭]BeanProvider
	 */
	public void destroy() {
		if (beanProvider != null)
			beanProvider.close();
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// 只对http进行处理
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse res = (HttpServletResponse) response;
			HttpSession session = req.getSession(true);

			// 封装请求上下文
			AuthInfo info = this.authManager.doAuth(createAuthInfo(req,session));
			
			//将authinfo放进session
			session.setAttribute(AUTH_INFO_NAME_IN_SESSION, info);
			
			// 在此之前，所有的环境都必须已经设置好
			chain.doFilter(req, res);
		} else
			chain.doFilter(request, response);
	}
	
	private AuthInfo createAuthInfo(HttpServletRequest req, HttpSession session){
		return new AuthInfoImpl(req,session);
	}

	/**
	 * 初始化BeanProvider 需要检查： 1.是否实现BeanProvider接口 2.是否实现servletContextAware接口
	 */
	public void init(FilterConfig config) throws ServletException {

		// 查看是否需要更换IOC容器
		String IOCContainer = config.getInitParameter(BEAN_PROVIDER_PARAM_NAME);
		if (IOCContainer != null && IOCContainer.length() > 1)
			DEFAULT_BEAN_PROVIDER = IOCContainer;

		// 获取容器
		try {
			beanProvider = ReflectUtil.getObject(DEFAULT_BEAN_PROVIDER,
					BeanProvider.class);
		} catch (ReflectOpException e) {
			throw new RuntimeException("auth's StartFilter init fail.", e);
		}

		//给beanprovider提供servletcontext
		ServletContext context = config.getServletContext();
		injectContext(context);

		this.authManager = (Auth) beanProvider.getBean("authManager");
		if(this.authManager == null){
			throw new RuntimeException("can't get AuthBean[name=authManager,type=org.j2auth.main.Auth] from beanProvider.");
		}
	}

	/*
	 * 判断BeanProvider是否实现了ServletContextAware接口，若有，注入context。
	 */
	private void injectContext(ServletContext context) {
		Class<?> clazz = beanProvider.getClass();
		if (ReflectUtil.isInterface(clazz, ServletContextAware.class)) {
			// 注入context
			try {
				Method method = clazz.getMethod("setServletContext",
						ServletContext.class);
				method.invoke(beanProvider, context);
			} catch (Throwable e) {
				throw new RuntimeException("auth's StartFilter init fail.",
						e);
			}
		}
	}

}
