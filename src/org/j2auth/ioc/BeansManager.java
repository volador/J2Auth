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
 * ����bean������
 * @author volador
 *
 */
public class BeansManager extends AbstractBeansFactory{
	
	//ע��������
	private InjectorManager injectorManager = new InjectorManager();
	
	//ʵ������
	private Map<String,Object> beansHolder = new HashMap<String,Object>();
	
	//��������
	private Map<String,Produce> proEvn = new HashMap<String,Produce>();
	
	/**
	 * <pre>
	 * ������,����������Ʒ�������������ò�Ʒ����������
	 * ������֮�������������������һ�������������������г���������һ�������ߣ������������Ƚ���
	 * ��һ��������
	 * ���ڲ������ʽ���壬�����ȡ�ڲ���Դ
	 * </pre>
	 * @author volador
	 *
	 */
	class Producer implements Produce{ 

		//��Ʒ����
		private String productName;
		//��Ʒclass
		private String clazz;
		//��Ʒ����ע����
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
		 * �������ע����
		 * @param injector ע����
		 */
		public void addInjector(Injector injector){
			injectors.add(injector);
		}
		
		@Override
		public Object doProduce() {
			//��ȡ�ɾ���Object
			Object obj = getObject(clazz);
			//����ע��
			for(Injector injector : injectors){
				obj = injector.doInject(obj,BeansManager.this);
			}
			//��������
			beansHolder.put(productName, obj);
			//���ز�Ʒ
			return obj;
		}
		
		/**
		 * �������ַ������ʵ��,����������޲ι�����
		 * @param clazz ����������
		 * @return ��ʵ��
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
	 * �������ֶ�Ӧ��bean��䵽beansHolder�У�;������������������beanҲ����ȥ
	 * @param name ��Ҫ��������
	 * @return ���ֶ�Ӧ��bean
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
	
	//��ʼ����������
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
			//�õ����������ڵ�
			NodeList refNodes = node.getChildNodes();
			int length = refNodes.getLength();
			for(int i = 0 ; i < length ; i++){
				Node ref = refNodes.item(i);
				if(ref.getNodeType() != 1) continue; //���ηǽڵ�����
				//�����ڵ������
				String refTag = ref.getNodeName();
				//��ǰ����·��
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
		} catch (Exception e) {
			throw new RuntimeException("delay init error:can not init producer,check you beans config file.",e);
		}
		return xmlDoc;
	}

}
