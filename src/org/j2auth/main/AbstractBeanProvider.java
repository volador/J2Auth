package org.j2auth.main;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;


/**
 * 这东西是耦合j2ee API的最后位置了，下面的版块不能再耦合了。
 * @author volador
 *
 */
public abstract class AbstractBeanProvider implements ServletContextAware,BeanProvider{
	
	private Map<String,String> initParams = new HashMap<String,String>();
	/**
	 * 获取启动参数
	 * @param name 参数名字
	 * @return 参数值
	 */
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
	
	/**
	 * 延迟的初始化，之所以要延迟，因为要先做相关的设置后，才能给子类提供一些初始化的参数
	 */
	protected void doInit(){};
	
	@Override
	public void close(){}
}
