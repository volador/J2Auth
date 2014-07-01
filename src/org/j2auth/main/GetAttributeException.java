package org.j2auth.main;
/**
 * 获取属性出错
 * @author volador
 *
 */
public class GetAttributeException extends Exception {
	private static final long serialVersionUID = 1L;

	public GetAttributeException(Exception otherwise) {
		super(otherwise);
	}

}
