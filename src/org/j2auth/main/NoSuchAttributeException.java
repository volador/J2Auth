package org.j2auth.main;

/**
 * 指定对象里面没有对应的属性
 * @author volador
 *
 */
public class NoSuchAttributeException extends Exception {
	public NoSuchAttributeException(String clazz, String attribute) {
		super("can not find attribute[name="+attribute+"] in " + clazz);
	}
	
	public NoSuchAttributeException() {}
	private static final long serialVersionUID = 1L;
}
