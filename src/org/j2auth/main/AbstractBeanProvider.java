package org.j2auth.main;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
/**
 * 抽象BeanProvider，屏蔽接口复杂度
 * @author volador
 *
 */
public abstract class AbstractBeanProvider implements ServletContextAware,BeanProvider{
	
	private Map<String,String> initParams = new HashMap<String,String>();

	protected Object getInitParam(String name){
		return initParams.get(name);
	}
	
	protected Map<String,String> getInitParams(){
		return initParams;
	}
	
	@Override
	public void setServletContext(ServletContext context) {
		Enumeration<?> enus = context.getInitParameterNames();
		while(enus.hasMoreElements()){
			String key = (String) enus.nextElement();
			String value = (String) context.getAttribute(key);
			initParams.put(key, value);
		}
		doInit();
	}
	
	protected void doInit(){};
	
	@Override
	public void close(){}
}
