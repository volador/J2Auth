package org.j2auth.ioc.inject;

import java.io.FileNotFoundException;  
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.j2auth.util.XPath;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * 注入器管理者 负责初始化并维护注入链
 * @author volador
 *
 */
public class InjectorManager {

	//注入器集合[expect tag name：class]
	private Map<String, Class<? extends Injector>> injectors = new HashMap<String,Class<? extends Injector>>();

	@SuppressWarnings("unchecked")
	public InjectorManager() {
		//注入器配置文件
		InputStream stream = this.getClass().getResourceAsStream("injectors.xml");
		if(stream == null) throw new RuntimeException(new FileNotFoundException("can not find injectors.xml"));
		Document doc = getDocument(stream);
		List<Node> nodes = XPath.selectNodes("/injectors/injector", doc);
		for(Node node : nodes){
			String expectTag = XPath.selectText("@expectTag", node);
			String clazzName = XPath.selectText("@class", node);
			try {
				Class<? extends Injector> clazz = (Class<? extends Injector>) Class.forName(clazzName);
				injectors.put(expectTag, clazz);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private Document getDocument(InputStream input) {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		domFactory.setNamespaceAware(true); // never forget this!
		Document xmlDoc = null;
		DocumentBuilder builder;
		try {
			builder = domFactory.newDocumentBuilder();
			xmlDoc = builder.parse(input);
		} catch (Exception e) {
			throw new RuntimeException("can not load injectors.xml.",e);
		}
		return xmlDoc;
	}

	/**
	 * 根据tagname获取注入器实例，比如tagname=&lt;string>,则由StringInjector来处理
	 * @param tagName 注入器关注的tag
	 * @param nodeInfo bean.xml中bean定义节点信息
	 * @param currentPath bean.xml中bean定义节点位置
	 * @return 注入器实例
	 */
	public Injector getInjectorByExpectTag(String tagName,Node nodeInfo,String currentPath) {
		Class<? extends Injector> injectorClass = injectors.get(tagName);
		if(injectorClass == null) throw new RuntimeException("can not find out an injector[tagName="+tagName+"].");
		return newInstance(injectorClass, nodeInfo,currentPath);
	}

	private Injector newInstance(Class<? extends Injector> clazz,Node nodeInfo,String currentPath) {
		Injector injector = null;
		try {
			injector = clazz.newInstance();
			Method method = clazz.getMethod("nodeInfo",Node.class,String.class);
			method.invoke(injector, nodeInfo,currentPath);
		} catch (Exception e) {
			throw new RuntimeException("can not instance injector:" + clazz, e);
		}
		return injector;
	}
	
	@Override
	public String toString() {
		return this.injectors.toString();
	}
}
