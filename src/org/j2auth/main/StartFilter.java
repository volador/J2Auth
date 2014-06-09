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
 * 启动器
 * @author volador
 *
 */
public class StartFilter implements Filter {
	//web.xml配置
	private static final String BEAN_PROVIDER_PARAM_NAME = "BeanProvider";
	
	//权控管理器bean在ioc容器中的名字
	private static final String AUTH_PROCESSER_NAME_IN_IOC = "authManager";
	
	//默认的ioc容器
	private static String DEFAULT_BEAN_PROVIDER = "org.j2auth.main.LocalIOCAdapter";

	protected BeanProvider beanProvider = null;
	protected Auth authManager = null;
	
	/**
	 * 关闭bean容器
	 */
	public void destroy() {
		if (beanProvider != null)
			beanProvider.close();
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		//只针对http请求
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse res = (HttpServletResponse) response;
			HttpSession session = req.getSession(true);
			
			//权控执行
			AuthContext authContext = this.authManager.doAuth(new AuthContextImpl(req,res));
			
			session.setAttribute(AuthContext.SESSION, authContext);
			
			chain.doFilter(req, res);
		} else
			chain.doFilter(request, response);
	}

	/**
	 * 初始化bean容器
	 */
	public void init(FilterConfig config) throws ServletException {

		//可以在web.xml中更改ioc容器
		String IOCContainer = config.getInitParameter(BEAN_PROVIDER_PARAM_NAME);
		
		IOCContainer = (IOCContainer != null && IOCContainer.length() > 1) ? IOCContainer : DEFAULT_BEAN_PROVIDER;

		try {
			beanProvider = ReflectUtil.getObject(IOCContainer,BeanProvider.class);
		} catch (ReflectOpException e) {
			throw new RuntimeException("auth's StartFilter init fail.", e);
		}

		ServletContext context = config.getServletContext();
		injectContext(context);

		this.authManager = (Auth) beanProvider.getBean(AUTH_PROCESSER_NAME_IN_IOC);
		if(this.authManager == null){
			throw new RuntimeException("can't get AuthBean[name=authManager,type=org.j2auth.main.Auth] from beanProvider.");
		}
	}

	/**
	 * 看ioc容器是否需要注入ServletContext ：是否实现ServletContextAware接口
	 * @see org.j2auth.main.ServletContextAware
	 * @param context 注入的请求上下文
	 */
	private void injectContext(ServletContext context) {
		Class<?> clazz = beanProvider.getClass();
		if (ReflectUtil.isInterface(clazz, ServletContextAware.class)) {
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
