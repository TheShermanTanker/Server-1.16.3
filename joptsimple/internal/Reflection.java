package joptsimple.internal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import joptsimple.ValueConverter;

public final class Reflection {
    private Reflection() {
        throw new UnsupportedOperationException();
    }
    
    public static <V> ValueConverter<V> findConverter(final Class<V> clazz) {
        final Class<V> maybeWrapper = Classes.<V>wrapperOf(clazz);
        final ValueConverter<V> valueOf = Reflection.valueOfConverter((java.lang.Class<Object>)maybeWrapper);
        if (valueOf != null) {
            return valueOf;
        }
        final ValueConverter<V> constructor = Reflection.constructorConverter((java.lang.Class<Object>)maybeWrapper);
        if (constructor != null) {
            return constructor;
        }
        throw new IllegalArgumentException(new StringBuilder().append(clazz).append(" is not a value type").toString());
    }
    
    private static <V> ValueConverter<V> valueOfConverter(final Class<V> clazz) {
        try {
            final Method valueOf = clazz.getMethod("valueOf", new Class[] { String.class });
            if (meetsConverterRequirements(valueOf, clazz)) {
                return new MethodInvokingValueConverter<V>(valueOf, clazz);
            }
            return null;
        }
        catch (NoSuchMethodException ignored) {
            return null;
        }
    }
    
    private static <V> ValueConverter<V> constructorConverter(final Class<V> clazz) {
        try {
            return new ConstructorInvokingValueConverter<V>((java.lang.reflect.Constructor<V>)clazz.getConstructor(new Class[] { String.class }));
        }
        catch (NoSuchMethodException ignored) {
            return null;
        }
    }
    
    public static <T> T instantiate(final Constructor<T> constructor, final Object... args) {
        try {
            return (T)constructor.newInstance(args);
        }
        catch (Exception ex) {
            throw reflectionException(ex);
        }
    }
    
    public static Object invoke(final Method method, final Object... args) {
        try {
            return method.invoke(null, args);
        }
        catch (Exception ex) {
            throw reflectionException(ex);
        }
    }
    
    public static <V> V convertWith(final ValueConverter<V> converter, final String raw) {
        return (V)((converter == null) ? raw : converter.convert(raw));
    }
    
    private static boolean meetsConverterRequirements(final Method method, final Class<?> expectedReturnType) {
        final int modifiers = method.getModifiers();
        return Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && expectedReturnType.equals(method.getReturnType());
    }
    
    private static RuntimeException reflectionException(final Exception ex) {
        if (ex instanceof IllegalArgumentException) {
            return new ReflectionException((Throwable)ex);
        }
        if (ex instanceof InvocationTargetException) {
            return new ReflectionException(ex.getCause());
        }
        if (ex instanceof RuntimeException) {
            return (RuntimeException)ex;
        }
        return new ReflectionException((Throwable)ex);
    }
}
