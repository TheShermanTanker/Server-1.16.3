package io.netty.handler.ssl;

import java.lang.reflect.InvocationTargetException;
import io.netty.util.internal.PlatformDependent;
import javax.net.ssl.SSLEngine;
import java.lang.reflect.Method;

final class Conscrypt {
    private static final Method IS_CONSCRYPT_SSLENGINE;
    
    private static Method loadIsConscryptEngine() {
        try {
            final Class<?> conscryptClass = Class.forName("org.conscrypt.Conscrypt", true, ConscryptAlpnSslEngine.class.getClassLoader());
            return conscryptClass.getMethod("isConscrypt", new Class[] { SSLEngine.class });
        }
        catch (Throwable ignore) {
            return null;
        }
    }
    
    static boolean isAvailable() {
        return Conscrypt.IS_CONSCRYPT_SSLENGINE != null && PlatformDependent.javaVersion() >= 8;
    }
    
    static boolean isEngineSupported(final SSLEngine engine) {
        return isAvailable() && isConscryptEngine(engine);
    }
    
    private static boolean isConscryptEngine(final SSLEngine engine) {
        try {
            return (boolean)Conscrypt.IS_CONSCRYPT_SSLENGINE.invoke(null, new Object[] { engine });
        }
        catch (IllegalAccessException ignore) {
            return false;
        }
        catch (InvocationTargetException ex) {
            throw new RuntimeException((Throwable)ex);
        }
    }
    
    private Conscrypt() {
    }
    
    static {
        IS_CONSCRYPT_SSLENGINE = loadIsConscryptEngine();
    }
}
