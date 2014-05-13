package org.j2auth.util;
/**
 * 解密器执行标准
 * @author volador
 *
 */
public interface Decoder {
	/**
	 * 解密
	 * @param value 要解密的内容
	 * @return 解密后的结果
	 */
	String decode(String value);
}
