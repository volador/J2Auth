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
 * ����Ȩ�޿��ƵĹ�����
 * 
 * @author voaldor
 * 
 */
public class StartFilter implements Filter {

	// �����ʼ��������web.xml�е�param-name
	public static final String BEAN_PROVIDER_PARAM_NAME = "BeanProvider";
	//authinfo��session�е�����
	public static final String AUTH_INFO_NAME_IN_SESSION = "auth_info";

	// Ĭ��BeanProvider
	private static String DEFAULT_BEAN_PROVIDER = "org.j2auth.bootstrap.LocalIOCAdapter";

	// ioc����
	protected BeanProvider beanProvider = null;
	protected Auth authManager = null;

	/**
	 * ����[�ر�]BeanProvider
	 */
	public void destroy() {
		if (beanProvider != null)
			beanProvider.close();
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// ֻ��http���д���
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse res = (HttpServletResponse) response;
			HttpSession session = req.getSession(true);
			
			// ��װ����������
			AuthInfo info = this.authManager.doAuth(new AuthInfoImpl(req,res));
			
			//��authinfo�Ž�session
			session.setAttribute(AUTH_INFO_NAME_IN_SESSION, info);
			
			// �ڴ�֮ǰ�����еĻ����������Ѿ����ú�
			chain.doFilter(req, res);
		} else
			chain.doFilter(request, response);
	}

	/**
	 * ��ʼ��BeanProvider ��Ҫ��飺 1.�Ƿ�ʵ��BeanProvider�ӿ� 2.�Ƿ�ʵ��servletContextAware�ӿ�
	 */
	public void init(FilterConfig config) throws ServletException {

		// �鿴�Ƿ���Ҫ����IOC����
		String IOCContainer = config.getInitParameter(BEAN_PROVIDER_PARAM_NAME);
		if (IOCContainer != null && IOCContainer.length() > 1)
			DEFAULT_BEAN_PROVIDER = IOCContainer;

		// ��ȡ����
		try {
			beanProvider = ReflectUtil.getObject(DEFAULT_BEAN_PROVIDER,
					BeanProvider.class);
		} catch (ReflectOpException e) {
			throw new RuntimeException("auth's StartFilter init fail.", e);
		}

		//��beanprovider�ṩservletcontext
		ServletContext context = config.getServletContext();
		injectContext(context);

		this.authManager = (Auth) beanProvider.getBean("authManager");
		if(this.authManager == null){
			throw new RuntimeException("can't get AuthBean[name=authManager,type=org.j2auth.main.Auth] from beanProvider.");
		}
	}

	/*
	 * �ж�BeanProvider�Ƿ�ʵ����ServletContextAware�ӿڣ����У�ע��context��
	 */
	private void injectContext(ServletContext context) {
		Class<?> clazz = beanProvider.getClass();
		if (ReflectUtil.isInterface(clazz, ServletContextAware.class)) {
			// ע��context
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
