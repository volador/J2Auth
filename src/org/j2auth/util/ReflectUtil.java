package org.j2auth.util;

public class ReflectUtil {
	public static String getMethodName(String paramName) {
		if (paramName == null || paramName.length() <= 0)
			throw new IllegalArgumentException("paramName can not be null.");
		StringBuilder sb = new StringBuilder();
		sb.append("set");
		sb.append(paramName.substring(0, 1).toUpperCase()); // ����ĸ��д
		if (paramName.length() > 1)
			sb.append(paramName.substring(1));
		return sb.toString();
	}

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
