package org.j2auth.util;

/**
 * 用于反射操作的工具类
 * 
 * @author volador
 * 
 */
public class ReflectUtil {
	/**
	 * 根据属性名字获取其setter的方法名字
	 * 
	 * @param paramName
	 *            属性名字
	 * @return 该属性的setter方法名字
	 */
	public static String getMethodName(String paramName) {
		if (paramName == null || paramName.length() <= 0)
			throw new IllegalArgumentException("paramName can not be null.");
		StringBuilder sb = new StringBuilder();
		sb.append("set");
		sb.append(paramName.substring(0, 1).toUpperCase()); // 首字母大写
		if (paramName.length() > 1)
			sb.append(paramName.substring(1));
		return sb.toString();
	}

	/**
	 * 通过反射获取相关实例
	 * 
	 * @param clazzName
	 *            实例的完整类名
	 * @param clazzInterface
	 *            实例应该实现的接口
	 * @return 声明该接口的实例
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
	 * 判断某类是否实现了某接口
	 * @param c 类class
	 * @param szInterface 接口名字
	 * @return 
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
