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

public class StartFilter implements Filter {

	public static final String BEAN_PROVIDER_PARAM_NAME = "BeanProvider";
	public static final String AUTH_INFO_NAME_IN_SESSION = "auth_info";

	private static String DEFAULT_BEAN_PROVIDER = "org.j2auth.bootstrap.LocalIOCAdapter";

	protected BeanProvider beanProvider = null;
	protected Auth authManager = null;

	public void destroy() {
		if (beanProvider != null)
			beanProvider.close();
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse res = (HttpServletResponse) response;
			HttpSession session = req.getSession(true);
			
			AuthInfo info = this.authManager.doAuth(new AuthInfoImpl(req,res));
			
			session.setAttribute(AUTH_INFO_NAME_IN_SESSION, info);
			
			chain.doFilter(req, res);
		} else
			chain.doFilter(request, response);
	}

	public void init(FilterConfig config) throws ServletException {

		String IOCContainer = config.getInitParameter(BEAN_PROVIDER_PARAM_NAME);
		if (IOCContainer != null && IOCContainer.length() > 1)
			DEFAULT_BEAN_PROVIDER = IOCContainer;

		try {
			beanProvider = ReflectUtil.getObject(DEFAULT_BEAN_PROVIDER,
					BeanProvider.class);
		} catch (ReflectOpException e) {
			throw new RuntimeException("auth's StartFilter init fail.", e);
		}

		ServletContext context = config.getServletContext();
		injectContext(context);

		this.authManager = (Auth) beanProvider.getBean("authManager");
		if(this.authManager == null){
			throw new RuntimeException("can't get AuthBean[name=authManager,type=org.j2auth.main.Auth] from beanProvider.");
		}
	}

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
