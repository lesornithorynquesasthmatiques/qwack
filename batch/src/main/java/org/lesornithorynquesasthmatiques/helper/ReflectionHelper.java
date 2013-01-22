package org.lesornithorynquesasthmatiques.helper;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alexandre Dutra
 *
 */
public class ReflectionHelper {

	public static <A extends Annotation> Map<PropertyDescriptor, A> findPropertyAnnotations(Class<?> beanClass, Class<A> annotationClass) throws IntrospectionException {
		Map<PropertyDescriptor, A> anns = new HashMap<>();
		BeanInfo beanInfo = Introspector.getBeanInfo(beanClass, Object.class);
		PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor pd : pds) {
			A ann = findPropertyAnnotation(beanClass, pd, annotationClass);
			if (ann != null) {
				anns.put(pd, ann);
			}
		}
		return anns;
	}

	public static <A extends Annotation> A findPropertyAnnotation(Class<?> beanClass, PropertyDescriptor pd, Class<A> annotationClass) {
		A ann = null;
		Field f = findDeclaredField(beanClass, pd.getName());
		if (f != null) {
			ann = f.getAnnotation(annotationClass);
		}
		if (ann == null) {
			ann = pd.getReadMethod().getAnnotation(annotationClass);
		}
		if (ann == null) {
			ann = findTypeAnnotation(pd.getPropertyType(), annotationClass);
		}
		return ann;
	}

	public static <A extends Annotation> A findTypeAnnotation(Class<?> beanClass, Class<A> annotationClass) {
		A ann = null;
		// beanClass can be null for primitive types superclasses
		while (beanClass != null && beanClass != Object.class && ann == null) {
			ann = beanClass.getAnnotation(annotationClass);
			beanClass = beanClass.getSuperclass();
		}
		return ann;
	}

	public static <A extends Annotation> List<A> findTypeAnnotations(Class<?> beanClass, Class<A> annotationClass) {
		List<A> anns = new ArrayList<>();
		// beanClass can be null for primitive types superclasses
		while (beanClass != null && beanClass != Object.class) {
			A ann = beanClass.getAnnotation(annotationClass);
			if(ann != null) anns.add(ann);
			beanClass = beanClass.getSuperclass();
		}
		return anns;
	}

	private static Field findDeclaredField(Class<?> beanClass, String propertyName) {
		Field f = null;
		while (beanClass != Object.class) {
			try {
				f = beanClass.getDeclaredField(propertyName);
			} catch (SecurityException e) {
			} catch (NoSuchFieldException e) {
			}
			beanClass = beanClass.getSuperclass();
		}
		return f;
	}

	/**
	 * @return whether the type can be instantiated with a default constructor (in practice, this also tests that it's not abstract, but the name was
	 *         kept short for simplicity)
	 */
	public static boolean hasDefaultConstructor(Class<?> propertyType) {
		if (Modifier.isAbstract(propertyType.getModifiers())) return false;
		try {
			propertyType.getConstructor();
			return true;
		} catch (NoSuchMethodException e) {
			return false;
		}
	}

	public static <T> T newInstance(Class<T> type) {
		try {
			return type.newInstance();
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not instantiate <" + type.getName() + ">, does it have a default constructor?", e);
		}
	}

	public static <T> Object getFieldValue(T target, Field field) {
		field.setAccessible(true);
		try {
			return field.get(target);
		} catch (IllegalAccessException e) {
			throw new AssertionError("should not happen, field was explicitely made accessible");
		}
	}

	public static <T> void setFieldValue(T result, Field field, Object value) {
		field.setAccessible(true);
		try {
			field.set(result, value);
		} catch (IllegalAccessException e) {
			throw new AssertionError("should not happen, field was explicitely made accessible");
		}
	}

}
