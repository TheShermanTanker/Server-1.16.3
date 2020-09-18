package io.netty.handler.codec.serialization;

import io.netty.util.internal.PlatformDependent;
import java.lang.ref.Reference;
import java.util.Map;
import java.util.HashMap;

public final class ClassResolvers {
    public static ClassResolver cacheDisabled(final ClassLoader classLoader) {
        return new ClassLoaderClassResolver(defaultClassLoader(classLoader));
    }
    
    public static ClassResolver weakCachingResolver(final ClassLoader classLoader) {
        return new CachingClassResolver(new ClassLoaderClassResolver(defaultClassLoader(classLoader)), (Map<String, Class<?>>)new WeakReferenceMap((java.util.Map<Object, java.lang.ref.Reference<Object>>)new HashMap()));
    }
    
    public static ClassResolver softCachingResolver(final ClassLoader classLoader) {
        return new CachingClassResolver(new ClassLoaderClassResolver(defaultClassLoader(classLoader)), (Map<String, Class<?>>)new SoftReferenceMap((java.util.Map<Object, java.lang.ref.Reference<Object>>)new HashMap()));
    }
    
    public static ClassResolver weakCachingConcurrentResolver(final ClassLoader classLoader) {
        return new CachingClassResolver(new ClassLoaderClassResolver(defaultClassLoader(classLoader)), (Map<String, Class<?>>)new WeakReferenceMap((java.util.Map<Object, java.lang.ref.Reference<Object>>)PlatformDependent.newConcurrentHashMap()));
    }
    
    public static ClassResolver softCachingConcurrentResolver(final ClassLoader classLoader) {
        return new CachingClassResolver(new ClassLoaderClassResolver(defaultClassLoader(classLoader)), (Map<String, Class<?>>)new SoftReferenceMap((java.util.Map<Object, java.lang.ref.Reference<Object>>)PlatformDependent.newConcurrentHashMap()));
    }
    
    static ClassLoader defaultClassLoader(final ClassLoader classLoader) {
        if (classLoader != null) {
            return classLoader;
        }
        final ClassLoader contextClassLoader = PlatformDependent.getContextClassLoader();
        if (contextClassLoader != null) {
            return contextClassLoader;
        }
        return PlatformDependent.getClassLoader(ClassResolvers.class);
    }
    
    private ClassResolvers() {
    }
}
