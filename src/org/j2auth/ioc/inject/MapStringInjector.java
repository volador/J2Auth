package org.j2auth.ioc.inject;

import java.lang.reflect.Method; 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.j2auth.util.ReflectUtil;
import org.j2auth.util.XPath;
import org.w3c.dom.Node;
/**
 * 
 * <pre>
 * eg£º
 * &lt;map-string name="xxx"&gt;
 *  &lt;entry key="xxx" value="xxx"/&gt;
 *  .
 *  .
 * &lt;/map-string&gt;
 * </pre>
 * 
 * @author volador
 *
 */
public class MapStringInjector extends AbstractInjector{

	public static final String TAG = "map-string";
	private String name;
	private Map<String,String> entrys = new HashMap<String,String>();
	
	@Override
	public String toString() {
		return TAG;
	};

	@Override
	protected Object doInject(Object obj) {
		String methodName = ReflectUtil.getMethodName(name);
		try {
			Method method = obj.getClass().getMethod(methodName, Map.class);
			method.invoke(obj, entrys);
		} catch (Exception e) {
			throw new RuntimeException("can not inject map to obj.",e);
		}
		return obj;
	}

	@Override
	public void nodeInfo(Node node,String currentPath) {
		name = XPath.selectText("@name", node);
		String path = currentPath + "entry";
		List<Node> subNodes = XPath.selectNodes(path, node);
		for(Node entry : subNodes){
			String key = XPath.selectText("@key", entry);
			String value = XPath.selectText("@value", entry);
			entrys.put(key, value);
		}
	}
	
}
