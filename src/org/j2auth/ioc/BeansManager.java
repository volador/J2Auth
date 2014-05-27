package org.j2auth.ioc;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap; 
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.j2auth.ioc.inject.Injector;
import org.j2auth.ioc.inject.InjectorManager;
import org.j2auth.util.XPath;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * BeanFactory本地实现
 * @author volador
 *
 */
public class BeansManager extends AbstractBeansFactory{
	//注入器管理者
	private InjectorManager injectorManager = new InjectorManager();
	//bean容器
	private Map<String,Object> beansHolder = new HashMap<String,Object>();
	//生产环境
	private Map<String,Produce> proEvn = new HashMap<String,Produce>();
	//bean生产者
	class Producer implements Produce{ 
		//bean名字
		private String productName;
		//bean的class名字
		private String clazz;
		//生产该bean时，需要解决的依赖链
		private List<Injector> injectors = new ArrayList<Injector>();
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("[").append("name:"+productName+";").append("class:"+clazz+";");
			for(Injector injector : injectors){
				sb.append(injector + "-");
			}
			sb.append("]");
			return sb.toString();
		}
		
		public Producer(String beanName,String beanClass) {
			this.productName = beanName;
			this.clazz = beanClass;
		}
		

		public void addInjector(Injector injector){
			injectors.add(injector);
		}
		
		@Override
		public Object doProduce() {
			Object obj = getObject(clazz);
			//进行依赖链处理
			for(Injector injector : injectors){
				obj = injector.doInject(obj,BeansManager.this);
			}
			//将处理好的bean放进bean容器
			beansHolder.put(productName, obj);
			return obj;
		}
		
		/*
		 * 获取bean实例
		 */
		private Object getObject(String clazz) {
			Object o = null;
			try {
				Class<?> clz = Class.forName(clazz);
				//bean必须要有无参构造器
				o = clz.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return o;
		}
		
	}
	
	@Override
	public Object getBean(String name) { 
		Object bean = beansHolder.get(name);
		if(bean == null)
			bean = fillBean(name);
		return bean;
	}
	
	@Override
	public boolean clean() {
		if(this.beansHolder != null){
			this.beansHolder = new HashMap<String,Object>();
		}
		return true;
	}

	/**
	 * 生产bean实例，并放进bean容器
	 * @param name bean实例名字
	 * @return bean实例
	 */
	private Object fillBean(String name) { 
		Produce p = proEvn.get(name);
		if(p == null) return null;
		return p.doProduce(); 
	}

	public BeansManager(Map<String,String> params){ 
		super(params);
		initProEvn();
	}
	
	public BeansManager() {  
		initProEvn();
	}
	
	/**
	 * 根据配置文件初始化bean生产环境
	 */
	void initProEvn(){ 
		InputStream source = getFileLoader().loadFile();
		Node root = getRootNode("/beans",source);
		List<Node> nodes = XPath.selectNodes("/beans/bean", root);
		if(nodes == null){
			System.out.println("no beans define.");
			return;
		}
		for(Node node : nodes){
			String beanName = XPath.selectText("@name", node);
			String beanClass = XPath.selectText("@class", node);
			Producer p = new Producer(beanName, beanClass);
			if(proEvn.get(beanName) != null) throw new RuntimeException("bean[name="+beanName+"] init error:duplicated define.");
			NodeList refNodes = node.getChildNodes();
			int length = refNodes.getLength();
			for(int i = 0 ; i < length ; i++){
				Node ref = refNodes.item(i);
				if(ref.getNodeType() != 1) continue;
				String refTag = ref.getNodeName();
				String currentPath = "/beans/bean/" + refTag + "/";
				//获取bean依赖注入器链
				Injector injector = injectorManager.getInjectorByExpectTag(refTag,ref,currentPath);
				p.addInjector(injector);
			}
			proEvn.put(beanName, p);
		}
	}
	
	private Node getRootNode(String rootPath,InputStream stream){
		InputStream config = getFileLoader().loadFile();
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true); // never forget this!
		Document xmlDoc=null;
		DocumentBuilder builder = null;
		try {
			builder = domFactory.newDocumentBuilder();
			xmlDoc = builder.parse(config);
		} catch (Exception e) {
			throw new RuntimeException("delay init error:can not init producer,check you beans config file.",e);
		}
		return xmlDoc;
	}

}
