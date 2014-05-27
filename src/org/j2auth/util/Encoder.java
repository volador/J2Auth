package org.j2auth.util;
/**
 * 解密接口
 * @author volador
 *
 */
public interface Encoder {
	/**
	 * 解密
	 * @param value 解密前值
	 * @return 解密后值
	 */
	String encode(String value);
}
