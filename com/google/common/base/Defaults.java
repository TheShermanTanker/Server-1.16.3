package com.google.common.base;

import java.util.Collections;
import java.util.HashMap;
import javax.annotation.Nullable;
import java.util.Map;
import com.google.common.annotations.GwtIncompatible;

@GwtIncompatible
public final class Defaults {
    private static final Map<Class<?>, Object> DEFAULTS;
    
    private Defaults() {
    }
    
    private static <T> void put(final Map<Class<?>, Object> map, final Class<T> type, final T value) {
        map.put(type, value);
    }
    
    @Nullable
    public static <T> T defaultValue(final Class<T> type) {
        final T t = (T)Defaults.DEFAULTS.get(Preconditions.<Class<T>>checkNotNull(type));
        return t;
    }
    
    static {
        final Map<Class<?>, Object> map = (Map<Class<?>, Object>)new HashMap();
        Defaults.<Boolean>put(map, (java.lang.Class<Boolean>)Boolean.TYPE, false);
        Defaults.<Character>put(map, (java.lang.Class<Character>)Character.TYPE, '\0');
        Defaults.<Byte>put(map, (java.lang.Class<Byte>)Byte.TYPE, (Byte)0);
        Defaults.<Short>put(map, (java.lang.Class<Short>)Short.TYPE, (Short)0);
        Defaults.<Integer>put(map, (java.lang.Class<Integer>)Integer.TYPE, 0);
        Defaults.<Long>put(map, (java.lang.Class<Long>)Long.TYPE, 0L);
        Defaults.<Float>put(map, (java.lang.Class<Float>)Float.TYPE, 0.0f);
        Defaults.<Double>put(map, (java.lang.Class<Double>)Double.TYPE, 0.0);
        DEFAULTS = Collections.unmodifiableMap((Map)map);
    }
}
