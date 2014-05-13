package org.j2auth.ioc;

import java.io.IOException;  
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap; 
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.j2auth.ioc.inject.Injector;
import org.j2auth.ioc.inject.InjectorManager;
import org.j2auth.util.Debug;
import org.j2auth.util.XPath;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 简单的bean管理器,可以用springIOC来替代的
 * @author volador
 *
 */
public class BeansManager extends AbstractBeansFactory{
	
	//注入器管理
	private InjectorManager injectorManager = new InjectorManager();
	
	//实例管理
	private Map<String,Object> beansHolder = new HashMap<String,Object>();
	
	//生产环境
	private Map<String,Produce> proEvn = new HashMap<String,Produce>();
	
	/**
	 * <pre>
	 * 生产者,负责生产产品，并将生产所得产品放入容器。
	 * 生产者之间存在生产责任链，若一个生产者在生产过程中出现依赖另一个生产者，则将生产责任先交付
	 * 另一个生产者
	 * 以内部类的形式定义，方便获取内部资源
	 * </pre>
	 * @author volador
	 *
	 */
	class Producer implements Produce{ 

		//产品名字
		private String productName;
		//产品class
		private String clazz;
		//产品依赖注入器
		private List<Injector> injectors = new ArrayList<Injector>();
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			sb.append("name:"+productName+";");
			sb.append("class:"+clazz+";");
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
		
		/**
		 * 继续添加注入器
		 * @param injector 注入器
		 */
		public void addInjector(Injector injector){
			injectors.add(injector);
		}
		
		@Override
		public Object doProduce() {
			//获取干净的Object
			Object obj = getObject(clazz);
			//依赖注入
			for(Injector injector : injectors){
				obj = injector.doInject(obj,BeansManager.this);
			}
			//更新容器
			beansHolder.put(productName, obj);
			//返回产品
			return obj;
		}
		
		/**
		 * 根据名字反射出类实例,该类必须有无参构造器
		 * @param clazz 类完整名字
		 * @return 类实例
		 */
		private Object getObject(String clazz) {
			Object o = null;
			try {
				Class<?> clz = Class.forName(clazz);
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
	 * 根据名字对应的bean填充到beansHolder中，途中由于依赖所产生的bean也填充进去
	 * @param name 需要填充的名字
	 * @return 名字对应的bean
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
	
	//初始化生产环境
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
			//拿到所有依赖节点
			NodeList refNodes = node.getChildNodes();
			int length = refNodes.getLength();
			for(int i = 0 ; i < length ; i++){
				Node ref = refNodes.item(i);
				if(ref.getNodeType() != 1) continue; //屏蔽非节点属性
				//依赖节点的名字
				String refTag = ref.getNodeName();
				//当前解析路径
				String currentPath = "/beans/bean/" + refTag + "/";
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
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("delay init error:can not init producer,check you beans config file.",e);
		}catch (SAXException e) {
			throw new RuntimeException("delay init error:can not init producer,check you beans config file.",e);
		} catch (IOException e) {
			throw new RuntimeException("delay init error:can not init producer,check you beans config file.",e);
		}
		return xmlDoc;
	}

}
