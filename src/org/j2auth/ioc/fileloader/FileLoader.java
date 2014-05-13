package org.j2auth.ioc.fileloader;

import java.io.InputStream; 

/**
 * 加载配置文件,关于文件加载器，有一下一些限制：<br>
 * <pre>
 * 构造方法为一下2个之一:
 * 1.无参构造方法
 * 2.构造方法参数为：Map&lt;String,String>:配置信息
 * </pre>
 * @author volador
 *
 */
public interface FileLoader {
	/**
	 * 获取配置文件 
	 * @return InputStream流 
	 */
	InputStream loadFile();
}
