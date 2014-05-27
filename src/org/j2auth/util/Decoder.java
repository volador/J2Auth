package org.j2auth.util;
/**
 * 信息加密器接口
 * @author volador
 *
 */
public interface Decoder {
	/**
	 * 加密
	 * @param value 加密前值
	 * @return 加密后值
	 */
	String decode(String value);
}
