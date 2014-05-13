package org.j2auth.util;
/**
 * 加密器执行标准
 * @author volador
 *
 */
public interface Encoder {
	/**
	 * 执行加密
	 * @param value 要加密的内容
	 * @return 加密后的结果
	 */
	String encode(String value);
}
