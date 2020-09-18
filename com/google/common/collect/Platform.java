package com.google.common.collect;

import java.lang.reflect.Array;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
final class Platform {
    static <T> T[] newArray(final T[] reference, final int length) {
        final Class<?> type = reference.getClass().getComponentType();
        final T[] result = (T[])Array.newInstance((Class)type, length);
        return result;
    }
    
    static MapMaker tryWeakKeys(final MapMaker mapMaker) {
        return mapMaker.weakKeys();
    }
    
    private Platform() {
    }
}
