package org.j2auth.ioc.fileloader;

import java.io.InputStream; 
/**
 * 文件加载器接口，系统可以提供各种文件加载器实现，实现从网络/本地/等各种情况的配置文件加载
 * @author volador
 *
 */
public interface FileLoader {
	/**
	 * 加载文件
	 * @return 文件输入流
	 */
	InputStream loadFile();
}
