package org.j2auth.ioc.fileloader;

import java.util.Map;
/**
 * 文件加载器抽象父类
 * @author volador
 *
 */
public abstract class AbstractFileLoader implements FileLoader,InitParamsAware{
	private Map<String,String> initParams;
	@Override
	public void setInitParams(Map<String, String> params) {
		this.initParams = params;
	}
	protected String getInitParam(String name){
		return initParams.get(name);
	}
}
