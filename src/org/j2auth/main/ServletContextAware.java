package org.j2auth.main;

import javax.servlet.ServletContext;
/**
 * ServletContext的注入接口，在初始化Bean容器时，若bean容器实现了该接口，则将ServletContext注入<br>
 * 用于bean容器从servletcontext中获取初始化数据
 * @author volador
 *
 */
public interface ServletContextAware {
	/**
	 * 注入context
	 * @param context
	 */
	void setServletContext(ServletContext context);
}
