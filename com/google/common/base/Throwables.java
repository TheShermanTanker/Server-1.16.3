package com.google.common.base;

import java.lang.reflect.InvocationTargetException;
import java.util.AbstractList;
import java.util.Arrays;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import com.google.common.annotations.Beta;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.lang.reflect.Method;
import javax.annotation.Nullable;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class Throwables {
    @GwtIncompatible
    private static final String JAVA_LANG_ACCESS_CLASSNAME = "sun.misc.JavaLangAccess";
    @GwtIncompatible
    @VisibleForTesting
    static final String SHARED_SECRETS_CLASSNAME = "sun.misc.SharedSecrets";
    @Nullable
    @GwtIncompatible
    private static final Object jla;
    @Nullable
    @GwtIncompatible
    private static final Method getStackTraceElementMethod;
    @Nullable
    @GwtIncompatible
    private static final Method getStackTraceDepthMethod;
    
    private Throwables() {
    }
    
    @GwtIncompatible
    public static <X extends Throwable> void throwIfInstanceOf(final Throwable throwable, final Class<X> declaredType) throws X, Throwable {
        Preconditions.<Throwable>checkNotNull(throwable);
        if (declaredType.isInstance(throwable)) {
            throw (Throwable)declaredType.cast(throwable);
        }
    }
    
    @Deprecated
    @GwtIncompatible
    public static <X extends Throwable> void propagateIfInstanceOf(@Nullable final Throwable throwable, final Class<X> declaredType) throws X, Throwable {
        if (throwable != null) {
            Throwables.<Throwable>throwIfInstanceOf(throwable, (java.lang.Class<Throwable>)declaredType);
        }
    }
    
    public static void throwIfUnchecked(final Throwable throwable) {
        Preconditions.<Throwable>checkNotNull(throwable);
        if (throwable instanceof RuntimeException) {
            throw (RuntimeException)throwable;
        }
        if (throwable instanceof Error) {
            throw (Error)throwable;
        }
    }
    
    @Deprecated
    @GwtIncompatible
    public static void propagateIfPossible(@Nullable final Throwable throwable) {
        if (throwable != null) {
            throwIfUnchecked(throwable);
        }
    }
    
    @GwtIncompatible
    public static <X extends Throwable> void propagateIfPossible(@Nullable final Throwable throwable, final Class<X> declaredType) throws X, Throwable {
        Throwables.<Throwable>propagateIfInstanceOf(throwable, (java.lang.Class<Throwable>)declaredType);
        propagateIfPossible(throwable);
    }
    
    @GwtIncompatible
    public static <X1 extends Throwable, X2 extends Throwable> void propagateIfPossible(@Nullable final Throwable throwable, final Class<X1> declaredType1, final Class<X2> declaredType2) throws X1, X2, Throwable {
        Preconditions.<Class<X2>>checkNotNull(declaredType2);
        Throwables.<X1>propagateIfInstanceOf(throwable, declaredType1);
        Throwables.<X2>propagateIfPossible(throwable, declaredType2);
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @GwtIncompatible
    public static RuntimeException propagate(final Throwable throwable) {
        throwIfUnchecked(throwable);
        throw new RuntimeException(throwable);
    }
    
    public static Throwable getRootCause(Throwable throwable) {
        Throwable cause;
        while ((cause = throwable.getCause()) != null) {
            throwable = cause;
        }
        return throwable;
    }
    
    @Beta
    public static List<Throwable> getCausalChain(Throwable throwable) {
        Preconditions.<Throwable>checkNotNull(throwable);
        final List<Throwable> causes = (List<Throwable>)new ArrayList(4);
        while (throwable != null) {
            causes.add(throwable);
            throwable = throwable.getCause();
        }
        return (List<Throwable>)Collections.unmodifiableList((List)causes);
    }
    
    @GwtIncompatible
    public static String getStackTraceAsString(final Throwable throwable) {
        final StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter((Writer)stringWriter));
        return stringWriter.toString();
    }
    
    @Beta
    @GwtIncompatible
    public static List<StackTraceElement> lazyStackTrace(final Throwable throwable) {
        return (List<StackTraceElement>)(lazyStackTraceIsLazy() ? jlaStackTrace(throwable) : Collections.unmodifiableList(Arrays.asList((Object[])throwable.getStackTrace())));
    }
    
    @Beta
    @GwtIncompatible
    public static boolean lazyStackTraceIsLazy() {
        return Throwables.getStackTraceElementMethod != null & Throwables.getStackTraceDepthMethod != null;
    }
    
    @GwtIncompatible
    private static List<StackTraceElement> jlaStackTrace(final Throwable t) {
        Preconditions.<Throwable>checkNotNull(t);
        return (List<StackTraceElement>)new AbstractList<StackTraceElement>() {
            public StackTraceElement get(final int n) {
                return (StackTraceElement)invokeAccessibleNonThrowingMethod(Throwables.getStackTraceElementMethod, Throwables.jla, t, n);
            }
            
            public int size() {
                return (int)invokeAccessibleNonThrowingMethod(Throwables.getStackTraceDepthMethod, Throwables.jla, t);
            }
        };
    }
    
    @GwtIncompatible
    private static Object invokeAccessibleNonThrowingMethod(final Method method, final Object receiver, final Object... params) {
        try {
            return method.invoke(receiver, params);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException((Throwable)e);
        }
        catch (InvocationTargetException e2) {
            throw propagate(e2.getCause());
        }
    }
    
    @Nullable
    @GwtIncompatible
    private static Object getJLA() {
        try {
            final Class<?> sharedSecrets = Class.forName("sun.misc.SharedSecrets", false, (ClassLoader)null);
            final Method langAccess = sharedSecrets.getMethod("getJavaLangAccess", new Class[0]);
            return langAccess.invoke(null, new Object[0]);
        }
        catch (ThreadDeath death) {
            throw death;
        }
        catch (Throwable t) {
            return null;
        }
    }
    
    @Nullable
    @GwtIncompatible
    private static Method getGetMethod() {
        return getJlaMethod("getStackTraceElement", new Class[] { Throwable.class, Integer.TYPE });
    }
    
    @Nullable
    @GwtIncompatible
    private static Method getSizeMethod() {
        return getJlaMethod("getStackTraceDepth", new Class[] { Throwable.class });
    }
    
    @Nullable
    @GwtIncompatible
    private static Method getJlaMethod(final String name, final Class<?>... parameterTypes) throws ThreadDeath {
        try {
            return Class.forName("sun.misc.JavaLangAccess", false, (ClassLoader)null).getMethod(name, (Class[])parameterTypes);
        }
        catch (ThreadDeath death) {
            throw death;
        }
        catch (Throwable t) {
            return null;
        }
    }
    
    static {
        jla = getJLA();
        getStackTraceElementMethod = ((Throwables.jla == null) ? null : getGetMethod());
        getStackTraceDepthMethod = ((Throwables.jla == null) ? null : getSizeMethod());
    }
}
