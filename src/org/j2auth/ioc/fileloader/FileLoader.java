package org.j2auth.ioc.fileloader;

import java.io.InputStream; 

/**
 * ���������ļ�,�����ļ�����������һ��һЩ���ƣ�<br>
 * <pre>
 * ���췽��Ϊһ��2��֮һ:
 * 1.�޲ι��췽��
 * 2.���췽������Ϊ��Map&lt;String,String>:������Ϣ
 * </pre>
 * @author volador
 *
 */
public interface FileLoader {
	/**
	 * ��ȡ�����ļ� 
	 * @return InputStream�� 
	 */
	InputStream loadFile();
}
