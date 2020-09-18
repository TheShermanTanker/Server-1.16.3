package com.google.common.base;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.WeakHashMap;
import java.util.Iterator;
import java.util.EnumSet;
import java.util.HashMap;
import java.lang.reflect.Field;
import com.google.common.annotations.GwtIncompatible;
import java.lang.ref.WeakReference;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class Enums {
    @GwtIncompatible
    private static final Map<Class<? extends Enum<?>>, Map<String, WeakReference<? extends Enum<?>>>> enumConstantCache;
    
    private Enums() {
    }
    
    @GwtIncompatible
    public static Field getField(final Enum<?> enumValue) {
        final Class<?> clazz = enumValue.getDeclaringClass();
        try {
            return clazz.getDeclaredField(enumValue.name());
        }
        catch (NoSuchFieldException impossible) {
            throw new AssertionError(impossible);
        }
    }
    
    public static <T extends Enum<T>> Optional<T> getIfPresent(final Class<T> enumClass, final String value) {
        Preconditions.<Class<T>>checkNotNull(enumClass);
        Preconditions.<String>checkNotNull(value);
        return Platform.<T>getEnumIfPresent(enumClass, value);
    }
    
    @GwtIncompatible
    private static <T extends Enum<T>> Map<String, WeakReference<? extends Enum<?>>> populateCache(final Class<T> enumClass) {
        final Map<String, WeakReference<? extends Enum<?>>> result = (Map<String, WeakReference<? extends Enum<?>>>)new HashMap();
        for (final T enumInstance : EnumSet.allOf((Class)enumClass)) {
            result.put(enumInstance.name(), new WeakReference((Object)enumInstance));
        }
        Enums.enumConstantCache.put(enumClass, result);
        return result;
    }
    
    @GwtIncompatible
    static <T extends Enum<T>> Map<String, WeakReference<? extends Enum<?>>> getEnumConstants(final Class<T> enumClass) {
        synchronized (Enums.enumConstantCache) {
            Map<String, WeakReference<? extends Enum<?>>> constants = (Map<String, WeakReference<? extends Enum<?>>>)Enums.enumConstantCache.get(enumClass);
            if (constants == null) {
                constants = Enums.<Enum>populateCache((java.lang.Class<Enum>)enumClass);
            }
            return constants;
        }
    }
    
    public static <T extends Enum<T>> Converter<String, T> stringConverter(final Class<T> enumClass) {
        return (Converter<String, T>)new StringConverter((java.lang.Class<Enum>)enumClass);
    }
    
    static {
        enumConstantCache = (Map)new WeakHashMap();
    }
    
    private static final class StringConverter<T extends Enum<T>> extends Converter<String, T> implements Serializable {
        private final Class<T> enumClass;
        private static final long serialVersionUID = 0L;
        
        StringConverter(final Class<T> enumClass) {
            this.enumClass = Preconditions.<Class<T>>checkNotNull(enumClass);
        }
        
        @Override
        protected T doForward(final String value) {
            return (T)Enum.valueOf((Class)this.enumClass, value);
        }
        
        @Override
        protected String doBackward(final T enumValue) {
            return enumValue.name();
        }
        
        @Override
        public boolean equals(@Nullable final Object object) {
            if (object instanceof StringConverter) {
                final StringConverter<?> that = object;
                return this.enumClass.equals(that.enumClass);
            }
            return false;
        }
        
        public int hashCode() {
            return this.enumClass.hashCode();
        }
        
        public String toString() {
            return "Enums.stringConverter(" + this.enumClass.getName() + ".class)";
        }
    }
}
