package io.netty.util.internal;

import java.lang.reflect.AccessibleObject;

public final class ReflectionUtil {
    private ReflectionUtil() {
    }
    
    public static Throwable trySetAccessible(final AccessibleObject object, final boolean checkAccessible) {
        if (checkAccessible && !PlatformDependent0.isExplicitTryReflectionSetAccessible()) {
            return (Throwable)new UnsupportedOperationException("Reflective setAccessible(true) disabled");
        }
        try {
            object.setAccessible(true);
            return null;
        }
        catch (SecurityException e) {
            return (Throwable)e;
        }
        catch (RuntimeException e2) {
            return (Throwable)handleInaccessibleObjectException(e2);
        }
    }
    
    private static RuntimeException handleInaccessibleObjectException(final RuntimeException e) {
        if ("java.lang.reflect.InaccessibleObjectException".equals(e.getClass().getName())) {
            return e;
        }
        throw e;
    }
}
