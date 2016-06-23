/**
 * 
 */
package com.primeton.paas.openapi.base.uitl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public final class ArrayUtil {

	public static String[] NULL_STRINGS = new String[0];

	public static Object[] NULL_OBJECTS = new Object[0];

	@SuppressWarnings("rawtypes")
	public static Class[] NULL_CLASSES = new Class[0];

	private ArrayUtil() {
		super();
	}

	public static void copyArray(final Object[] sourceObjects,
			final Object[] targetObjects) throws IllegalArgumentException {
		if (ArrayUtils.isEmpty(sourceObjects)
				|| ArrayUtils.isEmpty(targetObjects)) {
			return;
		}
		if (sourceObjects.length != targetObjects.length) {
			throw new IllegalArgumentException(
					"The length of the two arrays must be equal");
		}
		System.arraycopy(sourceObjects, 0, targetObjects, 0,
				targetObjects.length);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String[] getStringArrayValues(final Object value) {
		if (value instanceof Collection) {
			final String[] stringArray = new String[((Collection) value).size()];
			Iterator<String> iterator = ((Collection) value).iterator();
			int index = 0;
			while (iterator.hasNext()) {
				Object t_Object = iterator.next();
				stringArray[index] = ObjectUtils.toString(t_Object, null);
				index++;
			}

			return stringArray;
		}
		if (value instanceof Object[]) {
			Object[] objects = (Object[]) value;
			return getStringArrayValues(objects);
		}
		return NULL_STRINGS;
	}

	public static String[] getStringArrayValues(final Object[] value) {
		if (ArrayUtils.isEmpty(value)) {
			return NULL_STRINGS;
		}

		final String[] stringArray = new String[value.length];

		for (int i = 0; i < value.length; i++) {
			Object object = value[i];
			stringArray[i] = ObjectUtils.toString(object, null);
		}

		return stringArray;
	}

	@SuppressWarnings("rawtypes")
	public static Object[] getArrayValues(final Object value) {
		if (value instanceof Object[]) {
			return (Object[]) value;
		}
		if (value instanceof Collection) {
			return ((Collection) value).toArray();
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public static boolean hasType(Object[] objects, Class clazz) {
		return hasType(objects, clazz, false);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean hasType(Object[] objects, Class clazz,
			boolean allowSuperType) {
		if (null == clazz) {
			return false;
		}
		if (ArrayUtils.isEmpty(objects)) {
			return false;
		}
		for (int i = 0; i < objects.length; i++) {
			Object object = objects[i];
			if (null == object) {
				continue;
			}
			if (allowSuperType) {
				if (clazz.isAssignableFrom(object.getClass())) {
					return true;
				}
			} else {
				if (clazz == object.getClass()) {
					return true;
				}
			}
		}
		return false;
	}

	public static Object[] sort(Object[] array) {
		if (ArrayUtils.isEmpty(array)) {
			return array;
		}
		Arrays.sort(array);
		return array;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isArray(Object object) {
		if (null == object) {
			return false;
		}
		Class clazz = object.getClass();
		return clazz.isArray();
	}

	public static boolean isEmpty(Object[] array) {
		if (array == null || array.length == 0) {
			return true;
		}
		return false;
	}

}
