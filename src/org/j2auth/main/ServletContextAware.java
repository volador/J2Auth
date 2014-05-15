package org.j2auth.main;

import javax.servlet.ServletContext;

/**
 * 实现该接口的BeanProdiver，可以在启动时由Bootstraper注入ServletContext<br>
 * @author volador
 *
 */
public interface ServletContextAware {
	/**
	 * 注入ServletContext
	 * @param context
	 */
	void setServletContext(ServletContext context);
}
