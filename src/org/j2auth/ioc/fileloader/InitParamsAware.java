package org.j2auth.ioc.fileloader;

import java.util.Map;

/**
 * 实现该接口的类需要获取启动参数
 * @author volador
 *
 */
public interface InitParamsAware {
	/**
	 * 设置启动参数
	 */
	void setInitParams(Map<String,String> params);
}
