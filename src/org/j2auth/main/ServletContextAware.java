package org.j2auth.main;

import javax.servlet.ServletContext;

/**
 * ʵ�ָýӿڵ�BeanProdiver������������ʱ��Bootstraperע��ServletContext<br>
 * @author volador
 *
 */
public interface ServletContextAware {
	/**
	 * ע��ServletContext
	 * @param context
	 */
	void setServletContext(ServletContext context);
}
