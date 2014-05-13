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
 * ע��������
 * 
 * @author volador
 * 
 */
public class InjectorManager {

	private Map<String, Class<? extends Injector>> injectors = new HashMap<String,Class<? extends Injector>>();

	/*
	 * ��ʼ��injector�б�
	 */
	@SuppressWarnings("unchecked")
	public InjectorManager() {
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
	 * ͨ��tag���ֻ�ȡ���Ӧ��ע����,ÿ�λ�ȡ�ģ������µ�ʵ��
	 * 
	 * @param tagName
	 *            Ŀǰ��ǩ����
	 * @return ע����ʵ��
	 */
	public Injector getInjectorByExpectTag(String tagName,Node nodeInfo,String currentPath) {
		Class<? extends Injector> injectorClass = injectors.get(tagName);
		if(injectorClass == null) throw new RuntimeException("can not find out an injector[tagName="+tagName+"].");
		return newInstance(injectorClass, nodeInfo,currentPath);
	}

	/*
	 * ����һ��ע��������ʵ��
	 */
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
