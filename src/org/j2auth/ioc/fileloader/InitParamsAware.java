package org.j2auth.ioc.fileloader;

import java.util.Map;
/**
 * 若文件加载器实现该接口，在初始化文件加载器的是否给其注入初始化文件
 * @author volador
 *
 */
public interface InitParamsAware {

	/**
	 * 注入初始化配置
	 * @param params 初始化配置键值对
	 */
	void setInitParams(Map<String,String> params);
}
