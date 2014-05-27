package org.j2auth.util;
/**
 * 反射操作异常
 * @author volador
 *
 */
public class ReflectOpException extends Exception{
	private static final long serialVersionUID = 1L;
	public ReflectOpException(String message) {
		super(message);
	}
	public ReflectOpException(Exception e) {
		super(e);
	}
}
