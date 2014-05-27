package org.j2auth.ioc.inject;

import java.lang.reflect.Method;

import org.j2auth.util.ReflectUtil;
import org.j2auth.util.XPath;
import org.w3c.dom.Node;
/**
 * 注入String
 *eg: 
 * &lt;string name="xxx" value="xxx"/>
 * @author volador
 *
 */
public class StringInjector extends AbstractInjector{

	public static final String TAG = "string";
	private String name = null;
	private String value = null;
	
	@Override
	public String toString() {
		return TAG;
	}

	@Override
	public void nodeInfo(Node node, String currentPath) {
		name = XPath.selectText("@name", node);
		value = XPath.selectText("@value", node);
	}

	@Override
	Object doInject(Object obj) {
		String methodName = ReflectUtil.getMethodName(name);
		try {
			Method method = obj.getClass().getMethod(methodName, String.class);
			method.invoke(obj, value);
		} catch (Exception e) {
			throw new RuntimeException("can not inject String to obj.",e);
		}
		return obj;
	}

}
