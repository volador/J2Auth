package org.j2auth.ioc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.j2auth.ioc.fileloader.FileLoader;
import org.j2auth.ioc.fileloader.InitParamsAware;
/**
 * BeanFactory抽象实现，准备好配置文件加载器。
 * @author volador
 *
 */
public abstract class AbstractBeansFactory implements BeanFactory{

	//默认配置文件加载器
	private static String DEFAULT_FILELOADER = "org.j2auth.ioc.fileloader.LocalConfigFileLoader";
	//可以通过初始化参数改变文件加载器（web.xml中配置）
	private static final String FILE_LOADER_TAG = "fileLoader";
	
	private Map<String,String> initParams = new HashMap<String,String>();
	
	private FileLoader fileLoader;
	
	protected String getInitParam(String name){
		return initParams.get(name);
	}
	protected Map<String,String> getInitParams(){
		return initParams;
	}
	
	public AbstractBeansFactory() {
		this.fileLoader = initFileLoader();		
	}

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
	 * 初始化文件加载器
	 */
	private FileLoader initFileLoader() {
		try {
			Class<?> clazz = Class.forName(DEFAULT_FILELOADER);
			try {
				Constructor<?> c = clazz.getConstructor(Map.class);
				return (FileLoader) c.newInstance(getInitParams());
			} catch (Exception e) {}
			
			Object obj = clazz.newInstance();
			
			Class<?>[] interfaces = clazz.getInterfaces();
			for(Class<?> ifc : interfaces){
				//给文件加载器注入初始化参数
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
