package com.google.gson.internal;

import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Method;

public abstract class UnsafeAllocator {
    public abstract <T> T newInstance(final Class<T> class1) throws Exception;
    
    public static UnsafeAllocator create() {
        try {
            final Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            final Field f = unsafeClass.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            final Object unsafe = f.get(null);
            final Method allocateInstance = unsafeClass.getMethod("allocateInstance", new Class[] { Class.class });
            return new UnsafeAllocator() {
                @Override
                public <T> T newInstance(final Class<T> c) throws Exception {
                    assertInstantiable(c);
                    return (T)allocateInstance.invoke(unsafe, new Object[] { c });
                }
            };
        }
        catch (Exception ex) {
            try {
                final Method getConstructorId = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", new Class[] { Class.class });
                getConstructorId.setAccessible(true);
                final int constructorId = (int)getConstructorId.invoke(null, new Object[] { Object.class });
                final Method newInstance = ObjectStreamClass.class.getDeclaredMethod("newInstance", new Class[] { Class.class, Integer.TYPE });
                newInstance.setAccessible(true);
                return new UnsafeAllocator() {
                    @Override
                    public <T> T newInstance(final Class<T> c) throws Exception {
                        assertInstantiable(c);
                        return (T)newInstance.invoke(null, new Object[] { c, constructorId });
                    }
                };
            }
            catch (Exception ex2) {
                try {
                    final Method newInstance2 = ObjectInputStream.class.getDeclaredMethod("newInstance", new Class[] { Class.class, Class.class });
                    newInstance2.setAccessible(true);
                    return new UnsafeAllocator() {
                        @Override
                        public <T> T newInstance(final Class<T> c) throws Exception {
                            assertInstantiable(c);
                            return (T)newInstance2.invoke(null, new Object[] { c, Object.class });
                        }
                    };
                }
                catch (Exception ex3) {
                    return new UnsafeAllocator() {
                        @Override
                        public <T> T newInstance(final Class<T> c) {
                            throw new UnsupportedOperationException(new StringBuilder().append("Cannot allocate ").append(c).toString());
                        }
                    };
                }
            }
        }
    }
    
    private static void assertInstantiable(final Class<?> c) {
        final int modifiers = c.getModifiers();
        if (Modifier.isInterface(modifiers)) {
            throw new UnsupportedOperationException("Interface can't be instantiated! Interface name: " + c.getName());
        }
        if (Modifier.isAbstract(modifiers)) {
            throw new UnsupportedOperationException("Abstract class can't be instantiated! Class name: " + c.getName());
        }
    }
}
