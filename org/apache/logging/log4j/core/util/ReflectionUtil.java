package org.apache.logging.log4j.core.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Member;
import java.util.Objects;

public final class ReflectionUtil {
    private ReflectionUtil() {
    }
    
    public static <T extends AccessibleObject> boolean isAccessible(final T member) {
        Objects.requireNonNull(member, "No member provided");
        return Modifier.isPublic(((Member)member).getModifiers()) && Modifier.isPublic(((Member)member).getDeclaringClass().getModifiers());
    }
    
    public static <T extends java.lang.reflect.AccessibleObject> void makeAccessible(final T member) {
        if (!ReflectionUtil.<T>isAccessible(member) && !((AccessibleObject)member).isAccessible()) {
            ((AccessibleObject)member).setAccessible(true);
        }
    }
    
    public static void makeAccessible(final Field field) {
        Objects.requireNonNull(field, "No field provided");
        if ((!ReflectionUtil.<Field>isAccessible(field) || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }
    
    public static Object getFieldValue(final Field field, final Object instance) {
        makeAccessible(field);
        if (!Modifier.isStatic(field.getModifiers())) {
            Objects.requireNonNull(instance, "No instance given for non-static field");
        }
        try {
            return field.get(instance);
        }
        catch (IllegalAccessException e) {
            throw new UnsupportedOperationException((Throwable)e);
        }
    }
    
    public static Object getStaticFieldValue(final Field field) {
        return getFieldValue(field, null);
    }
    
    public static void setFieldValue(final Field field, final Object instance, final Object value) {
        makeAccessible(field);
        if (!Modifier.isStatic(field.getModifiers())) {
            Objects.requireNonNull(instance, "No instance given for non-static field");
        }
        try {
            field.set(instance, value);
        }
        catch (IllegalAccessException e) {
            throw new UnsupportedOperationException((Throwable)e);
        }
    }
    
    public static void setStaticFieldValue(final Field field, final Object value) {
        setFieldValue(field, null, value);
    }
    
    public static <T> Constructor<T> getDefaultConstructor(final Class<T> clazz) {
        Objects.requireNonNull(clazz, "No class provided");
        try {
            final Constructor<T> constructor = (Constructor<T>)clazz.getDeclaredConstructor(new Class[0]);
            ReflectionUtil.<Constructor<T>>makeAccessible(constructor);
            return constructor;
        }
        catch (NoSuchMethodException ignored) {
            try {
                final Constructor<T> constructor2 = (Constructor<T>)clazz.getConstructor(new Class[0]);
                ReflectionUtil.<Constructor<T>>makeAccessible(constructor2);
                return constructor2;
            }
            catch (NoSuchMethodException e) {
                throw new IllegalStateException((Throwable)e);
            }
        }
    }
    
    public static <T> T instantiate(final Class<T> clazz) {
        Objects.requireNonNull(clazz, "No class provided");
        final Constructor<T> constructor = ReflectionUtil.<T>getDefaultConstructor(clazz);
        try {
            return (T)constructor.newInstance(new Object[0]);
        }
        catch (LinkageError | InstantiationException linkageError) {
            final Throwable t;
            final Throwable e = t;
            throw new IllegalArgumentException(e);
        }
        catch (IllegalAccessException e2) {
            throw new IllegalStateException((Throwable)e2);
        }
        catch (InvocationTargetException e3) {
            Throwables.rethrow(e3.getCause());
            throw new InternalError("Unreachable");
        }
    }
}
