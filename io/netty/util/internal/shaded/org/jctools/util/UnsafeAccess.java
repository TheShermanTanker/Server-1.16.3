package io.netty.util.internal.shaded.org.jctools.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import sun.misc.Unsafe;

public class UnsafeAccess {
    public static final boolean SUPPORTS_GET_AND_SET;
    public static final Unsafe UNSAFE;
    
    static {
        Unsafe instance;
        try {
            final Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            instance = (Unsafe)field.get(null);
        }
        catch (Exception ignored) {
            try {
                final Constructor<Unsafe> c = (Constructor<Unsafe>)Unsafe.class.getDeclaredConstructor(new Class[0]);
                c.setAccessible(true);
                instance = (Unsafe)c.newInstance(new Object[0]);
            }
            catch (Exception e) {
                SUPPORTS_GET_AND_SET = false;
                throw new RuntimeException((Throwable)e);
            }
        }
        boolean getAndSetSupport = false;
        try {
            Unsafe.class.getMethod("getAndSetObject", new Class[] { Object.class, Long.TYPE, Object.class });
            getAndSetSupport = true;
        }
        catch (Exception ex) {}
        UNSAFE = instance;
        SUPPORTS_GET_AND_SET = getAndSetSupport;
    }
}
