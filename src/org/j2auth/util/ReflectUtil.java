package org.j2auth.util;
/**
 * 反射操作工具
 * @author volador
 *
 */
public class ReflectUtil {
	
	/**
	 * 获取属性set方法名字
	 * @param paramName 属性名字
	 * @return 完整set方法名字
	 */
	public static String setter(String paramName) {
		return getterSetter(paramName, Method.SETTER);
	}
	/**
	 * 获取属性set方法名字
	 * @param paramName 属性名字
	 * @return 完整set方法名字
	 */
	public static String getter(String paramName){
		return getterSetter(paramName, Method.GETTER);
	}
	
	private static String getterSetter(String paramName,Method topCase){
		if (paramName == null || paramName.length() <= 0)
			throw new IllegalArgumentException("paramName can not be null.");
		
		StringBuilder sb = new StringBuilder();
		sb.append(topCase.toString());
		sb.append(paramName.substring(0, 1).toUpperCase());
		if (paramName.length() > 1)
			sb.append(paramName.substring(1));
		return sb.toString();
	}
	
	

	/**
	 * 反射获取class实例
	 * @param clazzName 完整class名字
	 * @param clazzInterface 实现的接口列表
	 * @return 实例
	 * @throws ReflectOpException clazzName的类并没有实现clazzInterface接口
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getObject(String clazzName, Class<T> clazzInterface)
			throws ReflectOpException {
		try {
			Class<?> clazz = Class.forName(clazzName);
			if(isInterface(clazz, clazzInterface.getClass()))
				throw new ReflectOpException("class["+clazzName+"] do not extend interface["+clazzInterface+"].");
			return (T) clazz.newInstance();
		} catch (ClassNotFoundException e) {
			throw new ReflectOpException(e);
		} catch (InstantiationException e) {
			throw new ReflectOpException(e);
		} catch (IllegalAccessException e) {
			throw new ReflectOpException(e);
		}
	}
	
	/**
	 * 递归判断某类是否实现某接口
	 * @param c 类class
	 * @param szInterface 接口class
	 * @return true/false
	 */
	public static boolean isInterface(Class<?> c, Class<?> szInterface) {
		Class<?>[] face = c.getInterfaces();
		for (int i = 0, j = face.length; i < j; i++) {
			if (face[i].equals(szInterface)) {
				return true;
			} else {
				Class<?>[] face1 = face[i].getInterfaces();
				for (int x = 0; x < face1.length; x++) {
					if (face1[x].equals(szInterface)) {
						return true;
					} else if (isInterface(face1[x], szInterface)) {
						return true;
					}
				}
			}
		}
		if (null != c.getSuperclass()) {
			return isInterface(c.getSuperclass(), szInterface);
		}
		return false;
	}
}

enum Method{
	GETTER{
		@Override
		public String toString() {
			return "get";
		}
	}
	,SETTER{
		@Override
		public String toString() {
			return "set";
		}
	}
}