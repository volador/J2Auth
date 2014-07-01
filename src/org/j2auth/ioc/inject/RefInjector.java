package org.j2auth.ioc.inject;

import java.lang.reflect.Method;

import org.j2auth.ioc.BeanFactory;
import org.j2auth.util.ReflectUtil;
import org.j2auth.util.XPath;
import org.w3c.dom.Node;

/**
 * 注入实例引用
 * <pre>
 * &lt;ref name="xxx" type="xxx"/&gt;
 * </pre>
 * @author volador
 *
 */
public class RefInjector extends AbstractInjector{

	public static final String TAG = "ref";
	private String refBeanName;
	
	private Class<?> type = Object.class;
	
	@Override
	public String toString() {
		return TAG;
	}

	@Override
	public void nodeInfo(Node node, String currentPath) {
		String beanName = XPath.selectText("@name", node);
		this.refBeanName = beanName;
		String typeName = XPath.selectText("@type", node);
		if(typeName != null && typeName.length() > 0){
			try {
				type = Class.forName(typeName);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("init RefInjector error:",e);
			}
		}
	}

	@Override
	Object doInject(Object obj) {
		BeanFactory factory = getBeanFactory();
		Object refBean = factory.getBean(refBeanName);
		if(refBean == null) throw new RuntimeException("can not get refBean[name="+refBeanName+"] from contain when init bean["+obj.getClass().getName()+"].");
		String methodName = ReflectUtil.setter(refBeanName);
		try {
			Method method = obj.getClass().getMethod(methodName, type);
			method.invoke(obj, refBean);
		} catch (Exception e) {
			throw new RuntimeException("can not inject ref to obj.",e);
		}
		return obj;
	}

}
