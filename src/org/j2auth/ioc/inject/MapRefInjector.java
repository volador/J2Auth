package org.j2auth.ioc.inject;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.j2auth.util.ReflectUtil;
import org.j2auth.util.XPath;
import org.w3c.dom.Node;
/**
 *  ×¢ÈëList&lt;Object&gt;
 *  <pre>
 *  ÓÃÀý£º
 *  &lt;map-ref name="xxx" type="xxxx"&gt;
 *    &lt;entry name="xxx" ref="xx"/&gt;
 *  &lt;/map-ref&gt;
 *  </pre>
 * @author volador
 *
 */
public class MapRefInjector extends AbstractInjector{

	public static final String TAG = "map-ref";
	private Map<String,Object> map = new LinkedHashMap<String,Object>();
	private String name = null;
	private Class<?> type = null;
	private Map<String,String> entrys = new LinkedHashMap<String,String>();
	
	@Override
	public String toString() {
		return TAG;
	}
	
	@Override
	public void nodeInfo(Node node, String currentPath) {
		name = XPath.selectText("@name", node);
		String typeName = XPath.selectText("@type", node);
		try {
			type = Class.forName(typeName);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("init MapRefInjector error.",e);
		}
		String path = currentPath + "entry";
		List<Node> nodes = XPath.selectNodes(path, node);
		if(nodes != null && nodes.size() > 0)
		for(Node n : nodes){
			String key = XPath.selectText("@name", n);
			String value = XPath.selectText("@ref", n);
			entrys.put(key, value);
		}
	}

	@Override
	Object doInject(Object obj) {
		initMap();
		String methodName = ReflectUtil.getMethodName(name);
		try {
			Method method = obj.getClass().getMethod(methodName, Map.class);
			method.invoke(obj, map);
		} catch (Exception e) {
			throw new RuntimeException("can not inject map to obj.",e);
		}
		return obj;
	}

	private void initMap() {
		for(Map.Entry<String, String> entry : entrys.entrySet()){
			String name = entry.getKey();
			String ref = entry.getValue();
			Object refObj = getBeanFactory().getBean(ref);
			if(refObj == null)
				throw new RuntimeException("init MapRefInjector error:can not find refObject[name="+ref+"]");
			if(!ReflectUtil.isInterface(refObj.getClass(), type))
				throw new RuntimeException("init MapRefInjector error:refObject[name="+ref+"] is not an instance of interface["+type+"].");
			map.put(name, refObj);
		}
	}

}
