package org.j2auth.ioc;

import java.lang.reflect.Constructor;    
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.j2auth.ioc.fileloader.FileLoader;
import org.j2auth.ioc.fileloader.InitParamsAware;
/**
 * ���ú�FileLoader,ʵ������·���ǣ���ʵ����manager->setServletContext->getFileLoader
 * @author volador
 *
 */
public abstract class AbstractBeansFactory implements BeanFactory{

	private static String DEFAULT_FILELOADER = "org.j2auth.ioc.fileloader.LocalConfigFileLoader";
	private static final String FILE_LOADER_TAG = "file_loader";
	
	private Map<String,String> initParams = new HashMap<String,String>();
	
	private FileLoader fileLoader;
	
	protected String getInitParam(String name){
		return initParams.get(name);
	}
	protected Map<String,String> getInitParams(){
		return initParams;
	}
	
	public AbstractBeansFactory() { //��ʼ��Ĭ�ϵ��ļ�������
		this.fileLoader = initFileLoader();		
	}
	/*
	 * ���Ը����ļ�������
	 */
	public AbstractBeansFactory(Map<String,String> params){
		initParams.putAll(params);
		String fileLoaderName = (String) getInitParam(FILE_LOADER_TAG);
		if(fileLoaderName != null && fileLoaderName.length() > 0){
			DEFAULT_FILELOADER = fileLoaderName;
		}
		this.fileLoader = initFileLoader();
	}
	
	protected FileLoader getFileLoader(){
		return fileLoader;
	}
	
	/*
	 * ��̬����fileLoader����������Ҫ�ж��Ƿ���Ҫע��InitParams���������£�
	 * 1.�Ƿ��й�����ע��
	 * 2.�Ƿ�ʵ�� InitParamsAware�ӿ�
	 */
	private FileLoader initFileLoader() {
		try {
			Class<?> clazz = Class.forName(DEFAULT_FILELOADER);
			//������ע�� 
			try {
				Constructor<?> c = clazz.getConstructor(Map.class);
				return (FileLoader) c.newInstance(getInitParams());
			} catch (Exception e) {}
			
			Object obj = clazz.newInstance();
			
			//ServletContextAwareע��
			Class<?>[] interfaces = clazz.getInterfaces();
			for(Class<?> ifc : interfaces){
				if(ifc.equals(InitParamsAware.class)){
					Method method = clazz.getMethod("setInitParams",Map.class);
					method.invoke(obj, getInitParams());
				}
			}
			return (FileLoader) obj;
		} catch (Exception e) {
			throw new RuntimeException("can not init FileLoader.",e);
		}
	}
}
