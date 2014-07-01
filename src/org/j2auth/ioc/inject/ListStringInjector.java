package org.j2auth.ioc.inject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.j2auth.util.ReflectUtil;
import org.j2auth.util.XPath;
import org.w3c.dom.Node;

/**
 * inject list&lt;String&gt;
 * <pre>
 * eg:
 * &lt;list-string name="xxx"&gt;
 *  &lt;value value="xxx"/&gt;
 *  .
 *  .
 * &lt;/list-string&gt;
 * </pre>
 * @author volador
 *
 */
public class ListStringInjector extends AbstractInjector{

	//just for showing
	public static final String TAG = "list-string";
	private List<String> list = new ArrayList<String>();
	private String name;
	
	@Override
	public String toString() {
		return TAG;
	}

	@Override
	public void nodeInfo(Node node, String currentPath) {
		name = XPath.selectText("@name", node);
		String path = currentPath + "value";
		List<Node> nodes = XPath.selectNodes(path, node);
		for(Node n : nodes){
			String value = XPath.selectText("@value", n);
			list.add(value);
		}
	}

	@Override
	protected Object doInject(Object obj) {
		String methodName = ReflectUtil.setter(name);
		try {
			Method method = obj.getClass().getMethod(methodName, List.class);
			method.invoke(obj, list);
		} catch (Exception e) {
			throw new RuntimeException("can not inject list to obj.",e);
		}
		return obj;
	}
	
}
